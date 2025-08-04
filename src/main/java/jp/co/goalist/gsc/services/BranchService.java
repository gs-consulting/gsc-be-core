package jp.co.goalist.gsc.services;

import io.jsonwebtoken.lang.Arrays;
import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.OemBranchRepository;
import jp.co.goalist.gsc.repositories.OperatorBranchRepository;
import jp.co.goalist.gsc.repositories.OperatorClientAccountRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.BranchMapper.BRANCH_MAPPER;
import static jp.co.goalist.gsc.mappers.DropdownMapper.DROPDOWN_MAPPER;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final OperatorBranchRepository operatorBranchRepository;
    private final OemBranchRepository oemBranchRepository;
    private final OemService oemService;
    private final OperatorClientAccountRepository operatorClientAccountRepository;
    private final ProjectService projectService;
    private final UtilService utilService;
    private final StoreService storeService;

    public List<OemBranch> getExistingOemBranchByIds(List<String> ids) {
        return oemBranchRepository.findAllById(ids);
    }

    public List<OperatorBranch> getExistingOperatorBranchByIds(List<String> ids) {
        return operatorBranchRepository.findAllById(ids);
    }

    private OperatorClientAccount getExistingOperatorClientAccountById(String id) {
        Optional<OperatorClientAccount> optionalOne = operatorClientAccountRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.OPERATOR_ACCOUNT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public ClientBranchDetailsDto getClientBranchDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorBranch operatorBranch = utilService.getExistingOperatorBranchById(id);
                return BRANCH_MAPPER.toClientOperatorBranchDetails(operatorBranch);
            }
            case Role.OEM -> {
                OemBranch oemBranch = utilService.getExistingOemBranchById(id);
                return BRANCH_MAPPER.toClientOemBranchDetails(oemBranch);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ClientBranchListDto getClientBranches(Integer pageNumber, Integer pageSize, String searchInput) {
        Account account = GeneralUtils.getCurrentUser();

        if (Objects.equals(account.getSubRole(), Role.OPERATOR.getId())) {
            OperatorClientAccount parent = utilService.getOperatorParent(account);
            Page<OperatorBranch> branches = operatorBranchRepository.findOperatorClientBranchBy(
                    parent.getId(),
                    GeneralUtils.wrapToLike(searchInput),
                    GeneralUtils.getPagination(pageNumber, pageSize));

            return ClientBranchListDto.builder()
                    .total(branches.getTotalElements())
                    .page(branches.getNumber() + 1)
                    .limit(branches.getSize())
                    .items(branches.getContent().stream().map(BRANCH_MAPPER::toClientBranchItemsDto).collect(Collectors.toList()))
                    .build();
        } else {
            OemClientAccount parent = utilService.getOemParent(account);
            Page<OemBranch> branches = oemBranchRepository.findOemClientBranchBy(
                    parent.getId(),
                    parent.getOemAccount().getId(),
                    parent.getOemGroupId(),
                    GeneralUtils.wrapToLike(searchInput),
                    GeneralUtils.getPagination(pageNumber, pageSize)
            );
            return ClientBranchListDto.builder()
                    .total(branches.getTotalElements())
                    .page(branches.getNumber() + 1)
                    .limit(branches.getSize())
                    .items(branches.getContent().stream().map(BRANCH_MAPPER::toClientBranchItemsDto).collect(Collectors.toList()))
                    .build();
        }
    }

    @Transactional
    public void createClientBranch(ClientBranchUpsertDto clientBranchUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        GeneralUtils.validateClientPermissions(Arrays.asList(new String[]{clientBranchUpsertDto.getStaffPermission()}), "社員の雇用形態権限");
        GeneralUtils.validateClientPermissions(Arrays.asList(new String[]{clientBranchUpsertDto.getPartTimePermission()}), "アルバイトの雇用形態権限");
        Triple<String, String, String> parentInfo = utilService.getParentIdAndOemIdAndGroupId(account);

        if (Objects.equals(account.getSubRole(), Role.OPERATOR.getId())) {
            OperatorBranch operatorBranch = BRANCH_MAPPER.mapToOperatorBranch(clientBranchUpsertDto);
            updateOperatorPrefecture(clientBranchUpsertDto.getPrefecture(), clientBranchUpsertDto.getCity(), operatorBranch);
            operatorBranch.setParentId(parentInfo.getLeft());
            operatorBranchRepository.saveAndFlush(operatorBranch);

            if (Objects.nonNull(clientBranchUpsertDto.getManagerRestrictions()) && !clientBranchUpsertDto.getManagerRestrictions().isEmpty()) {
                projectService.saveProjectNoBranchPermissionSetting(clientBranchUpsertDto.getManagerRestrictions());
            }
        } else {
            OemBranch oemBranch = BRANCH_MAPPER.mapToOemBranch(clientBranchUpsertDto);
            oemBranch.setOemGroupId(parentInfo.getRight());
            oemBranch.setOemParentId(parentInfo.getMiddle());
            oemBranch.setParentId(parentInfo.getLeft());
            updateOemPrefecture(clientBranchUpsertDto.getPrefecture(), clientBranchUpsertDto.getCity(), oemBranch);
            oemBranchRepository.saveAndFlush(oemBranch);

            if (Objects.nonNull(clientBranchUpsertDto.getManagerRestrictions()) && !clientBranchUpsertDto.getManagerRestrictions().isEmpty()) {
                projectService.saveProjectNoBranchPermissionSetting(clientBranchUpsertDto.getManagerRestrictions());
            }
        }
    }

    @Transactional
    public void editClientBranch(String id, ClientBranchUpsertDto clientBranchUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        GeneralUtils.validateClientPermissions(Arrays.asList(new String[]{clientBranchUpsertDto.getStaffPermission()}), "社員の雇用形態権限");
        GeneralUtils.validateClientPermissions(Arrays.asList(new String[]{clientBranchUpsertDto.getPartTimePermission()}), "アルバイトの雇用形態権限");

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorBranch operatorBranch = utilService.getExistingOperatorBranchById(id);
                BRANCH_MAPPER.updateOperatorBranch(operatorBranch, clientBranchUpsertDto);
                updateOperatorPrefecture(clientBranchUpsertDto.getPrefecture(), clientBranchUpsertDto.getCity(), operatorBranch);
                operatorBranchRepository.saveAndFlush(operatorBranch);

                if (Objects.nonNull(clientBranchUpsertDto.getManagerRestrictions()) && !clientBranchUpsertDto.getManagerRestrictions().isEmpty()) {
                    projectService.saveProjectNoBranchPermissionSetting(clientBranchUpsertDto.getManagerRestrictions());
                }
            }
            case Role.OEM -> {
                OemBranch oemBranch = utilService.getExistingOemBranchById(id);
                BRANCH_MAPPER.updateOemBranch(oemBranch, clientBranchUpsertDto);
                updateOemPrefecture(clientBranchUpsertDto.getPrefecture(), clientBranchUpsertDto.getCity(), oemBranch);
                oemBranchRepository.saveAndFlush(oemBranch);

                if (Objects.nonNull(clientBranchUpsertDto.getManagerRestrictions()) && !clientBranchUpsertDto.getManagerRestrictions().isEmpty()) {
                    projectService.saveProjectNoBranchPermissionSetting(clientBranchUpsertDto.getManagerRestrictions());
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public MDropdownListDto getOperatorBranches(Integer pageNumber, Integer pageSize, String clientAccountId, String storeId) {
        String parentId = null;

        if (Objects.nonNull(clientAccountId)) {
            parentId = clientAccountId;
        } else if (Objects.nonNull(storeId)) {
            OperatorStore store = utilService.getExistingOperatorStoreById(storeId);
            parentId = Objects.nonNull(store.getParentId()) ? store.getParentId() : null;
        }

        Page<OperatorBranch> branches = operatorBranchRepository.findOperatorBranchBy(
                parentId,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );
        return MDropdownListDto.builder()
                .total(branches.getTotalElements())
                .limit(branches.getSize())
                .page(branches.getNumber() + 1)
                .items(branches.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getOemBranches(Integer pageNumber, Integer pageSize, String clientAccountId, String storeId) {
        Account account = GeneralUtils.getCurrentUser();
        OemAccount oemAccount = oemService.getExistingById(account.getId());

        String parentId = null;

        if (Objects.nonNull(clientAccountId)) {
            parentId = clientAccountId;
        } else if (Objects.nonNull(storeId)) {
            OemStore store = utilService.getExistingOemStoreById(storeId);
            parentId = Objects.nonNull(store.getParentId()) ? store.getParentId() : null;
        }

        Page<OemBranch> branches = oemBranchRepository.findOemBranchBy(
                oemAccount.getOemGroup().getId(),
                oemAccount.getId(),
                parentId,
                null,
                GeneralUtils.getPagination(pageNumber, pageSize)
        );
        return MDropdownListDto.builder()
                .total(branches.getTotalElements())
                .limit(branches.getSize())
                .page(branches.getNumber() + 1)
                .items(branches.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                .build();
    }

    public MDropdownListDto getClientBranchesDropdown() {
        Account account = GeneralUtils.getCurrentUser();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount operatorClientAccount = getExistingOperatorClientAccountById(account.getId());
                Page<OperatorBranch> branches = operatorBranchRepository.findOperatorClientBranchBy(
                        Objects.nonNull(operatorClientAccount.getParent()) ? operatorClientAccount.getParent().getId() : operatorClientAccount.getId(),
                        null,
                        GeneralUtils.getPagination(null, null)
                );
                return MDropdownListDto.builder()
                        .total(branches.getTotalElements())
                        .limit(branches.getSize())
                        .page(branches.getNumber() + 1)
                        .items(branches.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            case Role.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                Page<OemBranch> branches = oemBranchRepository.findOemClientBranchBy(
                        parent.getId(),
                        parent.getOemAccount().getId(),
                        parent.getOemGroupId(),
                        null,
                        GeneralUtils.getPagination(null, null)
                );
                return MDropdownListDto.builder()
                        .total(branches.getTotalElements())
                        .limit(branches.getSize())
                        .page(branches.getNumber() + 1)
                        .items(branches.getContent().stream().map(DROPDOWN_MAPPER::toMDropdownItemsDto).collect(Collectors.toList()))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public Pair<Map<String, OperatorBranch>, Map<String, OemBranch>> getBranchesForCSV(String parentId,
                                                                                       String oemParentId,
                                                                                       String oemGroupId,
                                                                                       List<String> branchIds) {
        if (Objects.isNull(oemGroupId)) {
            List<OperatorBranch> branches = operatorBranchRepository.findBranchesBy(branchIds, parentId);
            return Pair.of(branches.stream().collect(Collectors.toMap(OperatorBranch::getId, b -> b)), null);
        } else {
            List<OemBranch> branches = oemBranchRepository.findBranchesBy(branchIds, parentId, oemParentId, oemGroupId);
            return Pair.of(null, branches.stream().collect(Collectors.toMap(OemBranch::getId, b -> b)));
        }
    }

    private void updateOemPrefecture(String prefectureId, String cityId, OemBranch oemBranch) {
        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        oemBranch.setPrefecture(prefectureCityPair.getLeft());
        oemBranch.setCity(prefectureCityPair.getRight());
    }

    private void updateOperatorPrefecture(String prefectureId, String cityId, OperatorBranch operatorBranch) {
        Pair<Prefecture, City> prefectureCityPair = utilService.findPrefectureAndCity(
                prefectureId,
                cityId
        );

        operatorBranch.setPrefecture(prefectureCityPair.getLeft());
        operatorBranch.setCity(prefectureCityPair.getRight());
    }

    @Transactional
    public void deleteSelectedBranches(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        List<String> branchIds = selectedIds.getSelectedIds();

        switch (Role.fromId(account.getSubRole())) {
            case Role.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                
                // First verify that all branches belong to the current user
                List<OperatorBranch> branches = operatorBranchRepository.findBranchesBy(branchIds, parent.getId());
                if (branches.size() != branchIds.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                            .message(ErrorMessage.INVALID_OPERATOR
                                    .getMessage())
                            .fieldError("selectedIds")
                            .build());
                }

                // Delete stores first (due to foreign key constraints)
                storeService.deleteStoresByBranchIds(branchIds, parent.getId(), null);
                
                // Then delete branches
                operatorBranchRepository.deleteBranchesByIds(branchIds, parent.getId());
            }
            case Role.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                
                // First verify that all branches belong to the current user
                List<OemBranch> branches = oemBranchRepository.findBranchesBy(
                        branchIds, parent.getId(), parent.getOemAccount().getId(), parent.getOemGroupId());
                if (branches.size() != branchIds.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                            .message(ErrorMessage.INVALID_OPERATOR
                                    .getMessage())
                            .fieldError("selectedIds")
                            .build());
                }
                
                // Delete stores first (due to foreign key constraints)
                storeService.deleteStoresByBranchIds(branchIds, parent.getId(), parent.getOemGroupId());
                
                // Then delete branches
                oemBranchRepository.deleteBranchesByIds(branchIds, parent.getId(), parent.getOemGroupId());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }
}
