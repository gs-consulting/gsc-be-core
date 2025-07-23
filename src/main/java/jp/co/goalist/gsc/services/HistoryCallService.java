package jp.co.goalist.gsc.services;

import static jp.co.goalist.gsc.mappers.HistoryCallMapper.HISTORY_CALL_MAPPER;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.entities.OemApplicant;
import jp.co.goalist.gsc.entities.OemClientAccount;
import jp.co.goalist.gsc.entities.OemHistoryCall;
import jp.co.goalist.gsc.entities.OperatorApplicant;
import jp.co.goalist.gsc.entities.OperatorClientAccount;
import jp.co.goalist.gsc.entities.OperatorHistoryCall;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.HistoryCallDetailsDto;
import jp.co.goalist.gsc.gen.dtos.HistoryCallItemsDto;
import jp.co.goalist.gsc.gen.dtos.HistoryCallMemoDto;
import jp.co.goalist.gsc.gen.dtos.HistoryCallUpsertDto;
import jp.co.goalist.gsc.gen.dtos.SelectedIds;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OemHistoryCallRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantRepository;
import jp.co.goalist.gsc.repositories.OperatorHistoryCallRepository;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryCallService {

    private final OemApplicantRepository oemApplicantRepo;
    private final OemHistoryCallRepository oemHistoryCallRepo;
    private final OperatorApplicantRepository operatorApplicantRepo;
    private final OperatorHistoryCallRepository operatorHistoryCallRepo;

    private final UtilService utilService;

    @Transactional
    public void createNewHistoryCall(String applicantId, HistoryCallUpsertDto historyCallUpsertDto) {
        validateForm(historyCallUpsertDto);
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                handleOperatorCreateNewHistoryCall(applicantId, historyCallUpsertDto, parentInfo.getLeft());
            }
            case OEM -> {
                handleOemCreateNewHistoryCall(applicantId,
                        historyCallUpsertDto, parentInfo.getLeft(), parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public List<HistoryCallItemsDto> getHistoryCalls(String applicantId) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<OperatorHistoryCall> operatorHistoryCalls = operatorHistoryCallRepo
                        .findByApplicantId(applicantId, parentInfo.getLeft());
                return operatorHistoryCalls.stream()
                        .map(HISTORY_CALL_MAPPER::toHistoryCallItemsDto)
                        .toList();
            }
            case OEM -> {
                List<OemHistoryCall> oemHistoryCalls = oemHistoryCallRepo
                        .findByApplicantId(
                                applicantId,
                                parentInfo.getLeft(),
                                parentInfo.getRight());
                return oemHistoryCalls.stream()
                        .map(HISTORY_CALL_MAPPER::toHistoryCallItemsDto)
                        .toList();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public HistoryCallDetailsDto getHistoryCallDetails(String applicantId, String id) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                return HISTORY_CALL_MAPPER.toHistoryCallDetailsDto(
                        getOpHistoryCallByIdAndParentId(
                                id,
                                applicantId,
                                parentInfo.getLeft()));
            }
            case OEM -> {
                return HISTORY_CALL_MAPPER.toHistoryCallDetailsDto(
                        getOemHistoryCallByIdAndParentId(
                                id,
                                applicantId,
                                parentInfo.getLeft(),
                                parentInfo.getRight()));
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editHistoryCall(String applicantId, String historyId, HistoryCallUpsertDto historyCallUpsertDto) {
        validateForm(historyCallUpsertDto);
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                handleOperatorEditHistoryCall(applicantId, historyId, historyCallUpsertDto, parentInfo.getLeft());
            }
            case OEM -> {
                handleOemEditHistoryCall(applicantId, historyId, historyCallUpsertDto, parentInfo.getLeft(), parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editHistoryCallMemo(String applicantId, String id, HistoryCallMemoDto historyCallMemoDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                OperatorHistoryCall historyCall = getOpHistoryCallByIdAndParentId(id, applicantId, parentInfo.getLeft());
                historyCall.setMemo(historyCallMemoDto.getMemo());
                operatorHistoryCallRepo.save(historyCall);
            }
            case OEM -> {
                OemHistoryCall historyCall = getOemHistoryCallByIdAndParentId(id, applicantId, parentInfo.getLeft(), parentInfo.getRight());
                historyCall.setMemo(historyCallMemoDto.getMemo());
                oemHistoryCallRepo.save(historyCall);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void deleteSelectedHistories(String applicantId, SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<OperatorHistoryCall> historyCalls = operatorHistoryCallRepo
                        .findAllByIdsAndApplicantId(selectedIds.getSelectedIds(), applicantId, parentInfo.getLeft());
                operatorHistoryCallRepo.deleteAll(historyCalls);
            }
            case OEM -> {
                List<OemHistoryCall> historyCalls = oemHistoryCallRepo
                        .findAllByIdsAndApplicantId(
                                selectedIds.getSelectedIds(),
                                applicantId,
                                parentInfo.getLeft(),
                                parentInfo.getRight());
                oemHistoryCallRepo.deleteAll(historyCalls);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private void validateForm(HistoryCallUpsertDto dto) {
        if (dto.getContactStartDateTime() == null || dto.getContactEndDateTime() == null) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), "通話日時"))
                    .fieldError("contactStartDate")
                    .build());
        }
        LocalDateTime startDate = GeneralUtils.parseDateTime(dto.getContactStartDateTime(), "contactStartDate",
                Constants.dateTimeNoSSFormatter);
        LocalDateTime endDate = GeneralUtils.parseDateTime(dto.getContactEndDateTime(), "contactEndDate",
                Constants.dateTimeNoSSFormatter);

        GeneralUtils.validateDateTimeRangeFields(startDate, endDate, "通話日時", "contactStartDate");
    }

    private void handleOperatorCreateNewHistoryCall(String applicantId, HistoryCallUpsertDto dto, String parentId) {
        OperatorHistoryCall historyCall = HISTORY_CALL_MAPPER.toOpHistoryCall(dto);

        OperatorApplicant applicant = getOpApplicantByIdAndParentId(applicantId, parentId);
        historyCall.setApplicant(applicant);

        OperatorClientAccount picAccount = utilService.getExistingOperatorClientAccountById(dto.getPicId());
        historyCall.setPic(picAccount);

        operatorHistoryCallRepo.save(historyCall);
    }

    private void handleOemCreateNewHistoryCall(String applicantId,
            HistoryCallUpsertDto dto, String parentId, String oemGroupId) {
        OemHistoryCall historyCall = HISTORY_CALL_MAPPER.toOemHistoryCall(dto);

        OemApplicant applicant = getOemApplicantByIdAndParentIdAndOemGroupId(applicantId, parentId, oemGroupId);
        historyCall.setApplicant(applicant);

        OemClientAccount picAccount = utilService.getExistingOemClientAccountById(dto.getPicId());
        historyCall.setPic(picAccount);

        oemHistoryCallRepo.save(historyCall);
    }

    private void handleOperatorEditHistoryCall(String applicantId, String historyId, HistoryCallUpsertDto dto,
            String parentId) {
        OperatorHistoryCall historyCall = getOpHistoryCallByIdAndParentId(historyId, applicantId, parentId);
        HISTORY_CALL_MAPPER.updateOpHistoryCall(dto, historyCall);

        OperatorClientAccount picAccount = utilService.getExistingOperatorClientAccountById(dto.getPicId());
        historyCall.setPic(picAccount);

        operatorHistoryCallRepo.save(historyCall);
    }

    private void handleOemEditHistoryCall(String applicantId, String historyId, HistoryCallUpsertDto dto,
            String parentId, String oemGroupId) {
        OemHistoryCall historyCall = getOemHistoryCallByIdAndParentId(historyId, applicantId, parentId, oemGroupId);
        HISTORY_CALL_MAPPER.updateOemHistoryCall(dto, historyCall);

        OemClientAccount picAccount = utilService.getExistingOemClientAccountById(dto.getPicId());
        historyCall.setPic(picAccount);

        oemHistoryCallRepo.save(historyCall);
    }

    private OperatorHistoryCall getOpHistoryCallByIdAndParentId(String historyId, String applicantId, String parentId) {
        Optional<OperatorHistoryCall> historyCall = operatorHistoryCallRepo
                .findOneBy(historyId, applicantId, parentId);
        if (historyCall.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.HISTORY_CALL.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return historyCall.get();
    }

    private OemHistoryCall getOemHistoryCallByIdAndParentId(String historyId, String applicantId, String parentId,
            String oemGroupId) {
        Optional<OemHistoryCall> historyCall = oemHistoryCallRepo
                .findOneBy(historyId, applicantId, parentId, oemGroupId);
        if (historyCall.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.HISTORY_CALL.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return historyCall.get();
    }

    private OperatorApplicant getOpApplicantByIdAndParentId(String applicantId, String parentId) {
        Optional<OperatorApplicant> applicant = operatorApplicantRepo.findByIdAndParentId(applicantId, parentId);
        if (applicant.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT.getTargetName()))
                    .fieldError("applicantId")
                    .build());
        }

        return applicant.get();
    }

    private OemApplicant getOemApplicantByIdAndParentIdAndOemGroupId(String applicantId, String parentId,
            String oemGroupId) {
        Optional<OemApplicant> applicant = oemApplicantRepo.findByIdAndParentIdAndOemGroupId(applicantId, parentId,
                oemGroupId);
        if (applicant.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT.getTargetName()))
                    .fieldError("applicantId")
                    .build());
        }

        return applicant.get();
    }
}
