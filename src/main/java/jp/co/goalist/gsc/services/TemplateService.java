package jp.co.goalist.gsc.services;

import static jp.co.goalist.gsc.mappers.TemplateMapper.TEMPLATE_MAPPER;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.OemClientAccount;
import jp.co.goalist.gsc.entities.OemTemplate;
import jp.co.goalist.gsc.entities.OperatorClientAccount;
import jp.co.goalist.gsc.entities.OperatorTemplate;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.MTemplateListDto;
import jp.co.goalist.gsc.gen.dtos.SelectedIds;
import jp.co.goalist.gsc.gen.dtos.TemplateDetailsDto;
import jp.co.goalist.gsc.gen.dtos.TemplateListDto;
import jp.co.goalist.gsc.gen.dtos.TemplateSearchDto;
import jp.co.goalist.gsc.gen.dtos.TemplateUpsertDto;
import jp.co.goalist.gsc.mappers.TemplateMapper;
import jp.co.goalist.gsc.repositories.OemTemplateRepository;
import jp.co.goalist.gsc.repositories.OperatorTemplateRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final Map<String, String> TEMPLATE_SORT_MAP = new HashMap<>() {
        {
            put("createdAt", "createdAt");
            put("updatedAt", "updatedAt");
            put("name", "templateName");
        }
    };

    private final UtilService utilService;
    private final OemTemplateRepository oemTemplateRepo;
    private final OperatorTemplateRepository operatorTemplateRepo;

    @Transactional
    public void createTemplate(TemplateUpsertDto templateUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorTemplate opTemplate = TEMPLATE_MAPPER.toOperatorTemplate(templateUpsertDto);
                opTemplate.setParentId(parentInfo.getLeft());
                operatorTemplateRepo.saveAndFlush(opTemplate);
            }
            case OEM -> {
                OemTemplate oemTemplate = TemplateMapper.TEMPLATE_MAPPER.toOemTemplate(templateUpsertDto);
                oemTemplate.setParentId(parentInfo.getLeft());
                oemTemplate.setOemGroupId(parentInfo.getRight());
                oemTemplateRepo.saveAndFlush(oemTemplate);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void deleteSelectedTemplates(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorClientAccount parentOperator = utilService.getOperatorParent(account);
                operatorTemplateRepo.deleteByParentAndIdIn(selectedIds.getSelectedIds(), parentOperator.getId());
            }
            case OEM -> {
                OemClientAccount parentOem = utilService.getOemParent(account);
                oemTemplateRepo.deleteByParentAndIdIn(selectedIds.getSelectedIds(), parentOem.getId(),
                        parentOem.getOemGroupId());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editTemplate(String id, TemplateUpsertDto templateUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorTemplate opTemplate = getOperatorTemplateById(id, parentInfo.getLeft());
                TEMPLATE_MAPPER.updateEntity(templateUpsertDto, opTemplate);
                operatorTemplateRepo.saveAndFlush(opTemplate);
            }
            case OEM -> {
                OemTemplate oemTemplate = getOemTemplateById(id, parentInfo.getLeft(), parentInfo.getRight());
                TEMPLATE_MAPPER.updateEntity(templateUpsertDto, oemTemplate);
                oemTemplateRepo.saveAndFlush(oemTemplate);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public TemplateDetailsDto getTemplateDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorTemplate opTemplate = getOperatorTemplateById(id, parentInfo.getLeft());
                return TEMPLATE_MAPPER.toOpDetailsDto(opTemplate);
            }
            case OEM -> {
                OemTemplate oemTemplate = getOemTemplateById(id, parentInfo.getLeft(), parentInfo.getRight());
                return TEMPLATE_MAPPER.toOemDetailsDto(oemTemplate);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public TemplateListDto getTemplateList(TemplateSearchDto templateSearchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                templateSearchDto.getPageNumber(),
                templateSearchDto.getPageSize(),
                getSortBy(templateSearchDto.getArrangedBy()));

        String searchPattern = GeneralUtils.wrapToLike(templateSearchDto.getSearchInput());
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        // TODO: isDeletable
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                Page<OperatorTemplate> opTemplateList = operatorTemplateRepo.findAllBy(
                        parentInfo.getLeft(),
                        searchPattern,
                        pageable);

                return TemplateListDto.builder()
                        .items(opTemplateList.getContent().stream()
                                .map(TEMPLATE_MAPPER::toOpItemsDto)
                                .toList())
                        .total(opTemplateList.getTotalElements())
                        .page(opTemplateList.getNumber() + 1)
                        .limit(opTemplateList.getSize())
                        .build();
            }
            case OEM -> {
                Page<OemTemplate> oemTemplateList = oemTemplateRepo.findAllBy(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        searchPattern,
                        pageable);
                
                return TemplateListDto.builder()
                        .items(oemTemplateList.getContent().stream()
                                .map(TEMPLATE_MAPPER::toOemItemsDto)
                                .toList())
                        .total(oemTemplateList.getTotalElements())
                        .page(oemTemplateList.getNumber() + 1)
                        .limit(oemTemplateList.getSize())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

    }

    @Transactional
    public MTemplateListDto getTemplatesForChat(TemplateSearchDto templateSearchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                templateSearchDto.getPageNumber(),
                templateSearchDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        String searchPattern = GeneralUtils.wrapToLike(templateSearchDto.getSearchInput());

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                Page<OperatorTemplate> operatorTemplates = operatorTemplateRepo.findAllBy(
                        parentInfo.getLeft(),
                        searchPattern,
                        pageable);

                return MTemplateListDto.builder()
                        .items(operatorTemplates.getContent().stream()
                                .map(TEMPLATE_MAPPER::toMTemplateItemsDto)
                                .toList())
                        .total(operatorTemplates.getTotalElements())
                        .page(operatorTemplates.getNumber() + 1)
                        .limit(operatorTemplates.getSize())
                        .build();
            }
            case OEM -> {
                Page<OemTemplate> oemTemplates = oemTemplateRepo.findAllBy(
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        searchPattern,
                        pageable);

                return MTemplateListDto.builder()
                        .items(oemTemplates.getContent().stream()
                                .map(TEMPLATE_MAPPER::toMTemplateItemsDto)
                                .toList())
                        .total(oemTemplates.getTotalElements())
                        .page(oemTemplates.getNumber() + 1)
                        .limit(oemTemplates.getSize())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public OperatorTemplate getOperatorTemplateById(String id, String parentId) {
        return operatorTemplateRepo.findById(id, parentId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.TEMPLATE.getTargetName()))
                        .fieldError("id")
                        .build()));
    }

    public OemTemplate getOemTemplateById(String id, String parentId, String oemGroupId) {
        return oemTemplateRepo.findById(id, parentId, oemGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                        .message(String.format(
                                ErrorMessage.NOT_FOUND.getMessage(),
                                TargetName.TEMPLATE.getTargetName()))
                        .fieldError("id")
                        .build()));
    }

    private Sort getSortBy(String arrangedBy) {
        if (arrangedBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] arrangedByArray = arrangedBy.split(":");
        if (arrangedByArray.length != 2 || !TEMPLATE_SORT_MAP.containsKey(arrangedByArray[0]) ||
                !Constants.SORT_ORDERS.containsKey(arrangedByArray[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .fieldError("arrangedBy")
                    .build());
        }

        return Sort.by(Sort.Direction.fromString(Constants.SORT_ORDERS.get(arrangedByArray[1])),
                TEMPLATE_SORT_MAP.get(arrangedByArray[0]));
    }
}
