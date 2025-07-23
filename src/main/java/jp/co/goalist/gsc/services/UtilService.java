package jp.co.goalist.gsc.services;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.StatusType;
import jp.co.goalist.gsc.enums.TargetName;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.clients.ClientConsts;
import jp.co.goalist.gsc.services.clients.dtos.EmailSendingRequestDto;
import jp.co.goalist.gsc.services.events.MailRegisterEvent;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final OemClientAccountRepository oemClientAccountRepository;
    private final OperatorClientAccountRepository operatorClientAccountRepository;
    private final OemAccountRepository oemAccountRepository;
    private final OemStoreRepository oemStoreRepository;
    private final OperatorStoreRepository operatorStoreRepository;
    private final OperatorBranchRepository operatorBranchRepository;
    private final OemBranchRepository oemBranchRepository;
    private final OemProjectRepository oemProjectRepository;
    private final OperatorProjectRepository operatorProjectRepository;
    private final InterviewCategoryRepository interviewCategoryRepository;
    private final MasterStatusRepository masterStatusRepository;
    private final BlacklistRepository blacklistRepository;
    private final PrefectureService prefectureService;
    private final ApplicationEventPublisher eventPublisher;
    private final OperatorAccountRepository operatorAccountRepo;

    public OemAccount getExistingOemAccountById(String id) {
        Optional<OemAccount> optionalOne = oemAccountRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.OEM_ACCOUNT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OperatorClientAccount getExistingOperatorClientAccountById(String id) {
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

    public OemClientAccount getExistingOemClientAccountById(String id) {
        Optional<OemClientAccount> optionalOne = oemClientAccountRepository.findById(id);

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

    public OemStore getExistingOemStoreById(String id) {
        Optional<OemStore> optionalOne = oemStoreRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.STORE.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OperatorStore getExistingOperatorStoreById(String id) {
        Optional<OperatorStore> optionalOne = operatorStoreRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.STORE.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OemBranch getExistingOemBranchById(String id) {
        Optional<OemBranch> optionalOne = oemBranchRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.BRANCH.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OperatorBranch getExistingOperatorBranchById(String id) {
        Optional<OperatorBranch> optionalOne = operatorBranchRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.BRANCH.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OemProject getOemProjectExistingById(String id) {
        Optional<OemProject> optionalOne = oemProjectRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.PROJECT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OperatorProject getOperatorProjectExistingById(String id) {
        Optional<OperatorProject> optionalOne = operatorProjectRepository.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.PROJECT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public Pair<Prefecture, City> findPrefectureAndCity(String prefectureId, String cityId) {
        Prefecture prefecture = null;
        City city = null;

        if (Objects.nonNull(cityId)) {
            try {
                city = prefectureService.getExistingCityById(Integer.parseInt(cityId));
            } catch (IllegalArgumentException e) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                cityId))
                        .fieldError("cityId")
                        .build());
            }
            try {
                if (Objects.nonNull(prefectureId)) {
                    if (Objects.equals(city.getPrefecture().getId(), Integer.parseInt(prefectureId))) {
                        prefecture = city.getPrefecture();
                    } else {
                        prefecture = prefectureService.getExistingPrefectureById(Integer.parseInt(prefectureId));
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                prefectureId))
                        .fieldError("prefectureId")
                        .build());
            }
        }
        return Pair.of(prefecture, city);
    }

    public Prefecture findPrefecture(String prefectureId) {
        Prefecture prefecture = null;
        try {
            if (Objects.nonNull(prefectureId)) {
                prefecture = prefectureService.getExistingPrefectureById(Integer.parseInt(prefectureId));
            }
        } catch (IllegalArgumentException e) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                            prefectureId))
                    .fieldError("prefectureId")
                    .build());
        }
        return prefecture;
    }

    public OemClientAccount getOemParent(Account account) {
        OemClientAccount current = getExistingOemClientAccountById(account.getId());
        return Objects.nonNull(current.getParent()) ? current.getParent() : current;
    }

    public OperatorClientAccount getOperatorParent(Account account) {
        OperatorClientAccount current = getExistingOperatorClientAccountById(account.getId());
        return Objects.nonNull(current.getParent()) ? current.getParent() : current;
    }

    public String getParentId(OperatorClientAccount clientAccount) {
        return Objects.nonNull(clientAccount.getParent()) ? clientAccount.getParent().getId() : clientAccount.getId();
    }

    public OemClientAccount getParent(OemClientAccount clientAccount) {
        return Objects.nonNull(clientAccount.getParent()) ? clientAccount.getParent() : clientAccount;
    }

    public String getParentId(OemClientAccount clientAccount) {
        return Objects.nonNull(clientAccount.getParent()) ? clientAccount.getParent().getId() : clientAccount.getId();
    }

    public Pair<String, String> getParentIdAndGroupId(Account account) {
        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                return Pair.of(null, null);
            }
            case Role.OEM -> {
                OemAccount oemAccount = getExistingOemAccountById(account.getId());
                OemAccount parent = Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent() : oemAccount;
                return Pair.of(parent.getId(), parent.getOemGroup().getId());
            }
            case Role.CLIENT -> {
                if (account.getSubRole().equals(Role.OPERATOR.getId())) {
                    OperatorClientAccount current = getExistingOperatorClientAccountById(account.getId());
                    return Pair.of(getParentId(current), null);
                } else if (account.getSubRole().equals(Role.OEM.getId())) {
                    OemClientAccount current = getExistingOemClientAccountById(account.getId());
                    return Pair.of(getParentId(current), current.getOemGroupId());
                } else {
                    throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public Triple<String, String, String> getParentIdAndOemIdAndGroupId(Account account) {
        switch (Role.fromId(account.getRole())) {
            case Role.OPERATOR -> {
                return null;
            }
            case Role.OEM -> {
                OemAccount oemAccount = getExistingOemAccountById(account.getId());
                OemAccount parent = Objects.nonNull(oemAccount.getParent()) ? oemAccount.getParent() : oemAccount;
                return Triple.of(parent.getId(), oemAccount.getId(), parent.getOemGroup().getId());
            }
            case Role.CLIENT -> {
                if (account.getSubRole().equals(Role.OPERATOR.getId())) {
                    OperatorClientAccount current = getExistingOperatorClientAccountById(account.getId());
                    return Triple.of(getParentId(current), null, null);
                } else if (account.getSubRole().equals(Role.OEM.getId())) {
                    OemClientAccount current = getExistingOemClientAccountById(account.getId());
                    return Triple.of(getParentId(current), current.getOemAccount().getId(), current.getOemGroupId());
                } else {
                    throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public InterviewCategory findInterviewCategoryById(String categoryId) {
        Optional<InterviewCategory> interviewCategory = interviewCategoryRepository.findById(categoryId);
        if (interviewCategory.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.INTERVIEW_CATEGORY.getTargetName()))
                    .fieldError("categoryId")
                    .build());
        }

        return interviewCategory.get();
    }

    public void publishMailRegisterEvent(String email) {
        EmailSendingRequestDto emailDto = EmailSendingRequestDto.builder()
                .email(email)
                .sendingType(ClientConsts.ST_ACCOUNT)
                .templateName(ClientConsts.TP_ACCOUNT_REGISTER)
                .jwt(GeneralUtils.getFromRequestAttribute("jwt"))
                .build();

        eventPublisher.publishEvent(new MailRegisterEvent(this, emailDto));
    }

    public List<MasterStatus> findMasterStatusesByType(List<String> ids, String oemGroupId, String parentId, StatusType type) {
        List<MasterStatus> statuses = masterStatusRepository.findStatusByIds(
                ids.stream().map(Long::parseLong).toList(),
                oemGroupId,
                parentId,
                type.getId()
        );

        if (statuses.isEmpty() || !Objects.equals(statuses.size(), ids.size())) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT_MASTER_DATA.getTargetName()))
                    .fieldError("ids")
                    .build());
        }

        return statuses;
    }

    public void updateApplicantBlacklistData(OemApplicant oemApplicant, OperatorApplicant operatorApplicant) {
        if (Objects.nonNull(oemApplicant)) {
            Optional<String> blackListId1 = blacklistRepository.findByFullNameAndTel(
                    oemApplicant.getFullName(),
                    oemApplicant.getTel()
            );
            Optional<String> blackListId2 = blacklistRepository.findByFullNameAndEmail(
                    oemApplicant.getFullName(),
                    oemApplicant.getEmail()
            );
            oemApplicant.setBlacklist1(blackListId1.orElse(null));
            oemApplicant.setBlacklist2(blackListId2.orElse(null));
        } else {
            Optional<String> blackListId1 = blacklistRepository.findByFullNameAndTel(
                    operatorApplicant.getFullName(),
                    operatorApplicant.getTel()
            );
            Optional<String> blackListId2 = blacklistRepository.findByFullNameAndEmail(
                    operatorApplicant.getFullName(),
                    operatorApplicant.getEmail()
            );
            operatorApplicant.setBlacklist1(blackListId1.orElse(null));
            operatorApplicant.setBlacklist2(blackListId2.orElse(null));
        }
    }

    public OperatorAccount getExistingOperatorAccountById(String id) {
        Optional<OperatorAccount> opAccount = operatorAccountRepo.findById(id);

        if (opAccount.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.OPERATOR_ACCOUNT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return opAccount.get();
    }
}
