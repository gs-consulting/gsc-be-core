package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.Blacklist;
import jp.co.goalist.gsc.entities.OemClientAccount;
import jp.co.goalist.gsc.entities.OperatorClientAccount;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.BlacklistRepository;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

import static jp.co.goalist.gsc.mappers.BlacklistMapper.BLACKLIST_MAPPER;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final Map<String, String> BLACKLIST_SORT_MAP = new HashMap<>() {
        {
            put("createdDateTime", "createdAt");
            put("fullName", "fullName");
            put("birthday", "birthday");
            put("tel", "tel");
            put("email", "email");
        }
    };

    private final Map<String, String> SORT_ORDERS = new HashMap<>() {
        {
            put("asc", Constants.SQL_ASC);
            put("desc", Constants.SQL_DESC);
        }
    };

    private final BlacklistRepository blacklistRepo;
    private final UtilService utilService;
    private final OemApplicantRepository oemApplicantRepo;
    private final OperatorApplicantRepository operatorApplicantRepo;

    @Transactional
    public BlacklistListDto getBlacklist(BlacklistSearchDto blacklistSearchDto) {
        Pageable pageable = GeneralUtils.getPagination(
                blacklistSearchDto.getPageNumber(),
                blacklistSearchDto.getPageSize(),
                getSortBy(blacklistSearchDto.getArrangedBy()));

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        Page<Blacklist> blacklistPage = blacklistRepo.findAllByParentIdAndOemGroupId(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                pageable);

        return BlacklistListDto.builder()
                .items(blacklistPage.getContent().stream()
                        .map(BLACKLIST_MAPPER::toBlacklistItemDto)
                        .toList())
                .total(blacklistPage.getTotalElements())
                .page(blacklistPage.getNumber() + 1)
                .limit(blacklistPage.getSize())
                .build();
    }

    @Transactional
    public void createNewBlacklist(BlacklistCreateDto blacklistCreateDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        Blacklist newBlacklist = BLACKLIST_MAPPER.toBlacklist(blacklistCreateDto);
        newBlacklist.setParentId(parentInfo.getLeft());
        newBlacklist.setOemGroupId(parentInfo.getRight());
        validateForm(blacklistCreateDto, parentInfo);

        Blacklist savedBlacklist = blacklistRepo.saveAndFlush(newBlacklist);
        updateBlacklistRelationship(
                savedBlacklist.getId(),
                parentInfo.getLeft(),
                parentInfo.getRight(),
                blacklistCreateDto.getFullName(),
                blacklistCreateDto.getTel(),
                blacklistCreateDto.getEmail()
        );
    }

    @Transactional
    public void editBlacklistMemo(String id, BlacklistMemoDto blacklistMemoDto) {
        Blacklist blacklist = getBlacklistExistingById(id);
        blacklist.setMemo(blacklistMemoDto.getMemo());
        blacklistRepo.saveAndFlush(blacklist);
    }

    @Transactional
    public void deleteSelectedBlacklists(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorClientAccount parentOperator = utilService.getOperatorParent(account);
                blacklistRepo.deleteByParentAndIdIn(parentOperator.getId(), null, selectedIds.getSelectedIds());
                updateBlacklistRelationshipWhenDelete(
                        selectedIds.getSelectedIds(),
                        parentOperator.getId(),
                        null
                );
            }
            case OEM -> {
                OemClientAccount parentOem = utilService.getOemParent(account);
                blacklistRepo.deleteByParentAndIdIn(parentOem.getId(), parentOem.getOemGroupId(), selectedIds.getSelectedIds());
                updateBlacklistRelationshipWhenDelete(
                        selectedIds.getSelectedIds(),
                        parentOem.getId(),
                        parentOem.getOemGroupId()
                );
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public void updateBlacklistRelationship(String blacklistId, String parentId, String oemGroupId, String fullName, String tel, String email) {
        if (Objects.isNull(oemGroupId)) {
            operatorApplicantRepo.updateBlacklistIdByApplicantInfo(blacklistId, parentId, fullName, tel, email);
        } else {
            oemApplicantRepo.updateBlacklistIdByApplicantInfo(blacklistId, parentId, oemGroupId, fullName, tel, email);
        }
    }

    public void updateBlacklistRelationshipWhenDelete(List<String> blacklistIds, String parentId, String oemGroupId) {
        if (Objects.isNull(oemGroupId)) {
            operatorApplicantRepo.updateBlacklistIdsToNull(blacklistIds, parentId);
        } else {
            oemApplicantRepo.updateBlacklistIdsToNull(blacklistIds, parentId, oemGroupId);
        }
    }

    public Blacklist getBlacklistExistingById(String id) {
        Optional<Blacklist> optionalOne = blacklistRepo.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.BLACKLIST.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    private Sort getSortBy(String arrangedBy) {
        if (arrangedBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] arrangedByArray = arrangedBy.split(":");
        if (arrangedByArray.length != 2 || !BLACKLIST_SORT_MAP.containsKey(arrangedByArray[0]) ||
                !SORT_ORDERS.containsKey(arrangedByArray[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .fieldError("arrangedBy")
                    .build());
        }

        return Sort.by(Sort.Direction.fromString(SORT_ORDERS.get(arrangedByArray[1])),
                BLACKLIST_SORT_MAP.get(arrangedByArray[0]));
    }

    private void validateForm(BlacklistCreateDto dto, Pair<String, String> parentInfo) {
        Optional<Blacklist> blacklist = blacklistRepo.findAllByApplicantInfo(
                dto.getFullName(),
                dto.getTel(),
                dto.getEmail(),
                parentInfo.getLeft(),
                parentInfo.getRight()
        );
        if (blacklist.isPresent()) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.DUPLICATE_DATA.getStatusCode())
                    .message(String.format(
                            ErrorMessage.DUPLICATE_DATA.getMessage(),
                            TargetName.BLACKLIST.getTargetName(),
                            dto.getFullName()
                    )).build());
        }
    }

}
