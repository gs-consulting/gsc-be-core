package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.applicant.ApplicantSearchItemsDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.*;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.MasterMediaRepository;
import jp.co.goalist.gsc.repositories.OemApplicantInterviewRepository;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantInterviewRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantRepository;
import jp.co.goalist.gsc.repositories.SelectionStatusRepository;
import jp.co.goalist.gsc.services.criteriaBuilder.ApplicantCriteriaBuilder;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.ApplicantMapper.APPLICANT_MAPPER;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantCriteriaBuilder applicantCriteriaBuilder;
    private final OemApplicantRepository oemApplicantRepo;
    private final OperatorApplicantRepository operatorApplicantRepo;
    private final SelectionStatusRepository selectionStatusRepo;
    private final UtilService utilService;
    private final MasterMediaRepository masterMediaRepository;

    @Transactional
    public ApplicantStatusCountDto countApplicationStatusNotChanged() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                Integer newApplications = operatorApplicantRepo.countStatusNotChangedApplications(parentInfo.getLeft(), false);
                return APPLICANT_MAPPER.toApplicantStatusCountDto(newApplications);
            }
            case SubRole.OEM -> {
                Integer newApplications = oemApplicantRepo.countStatusNotChangedApplications(parentInfo.getLeft(), parentInfo.getRight(), false);
                return APPLICANT_MAPPER.toApplicantStatusCountDto(newApplications);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ApplicantStatusCountDto countNewApplications() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                Integer newApplications = operatorApplicantRepo.countStatusNotChangedApplications(parentInfo.getLeft(), true);
                return APPLICANT_MAPPER.toApplicantStatusCountDto(newApplications);
            }
            case SubRole.OEM -> {
                Integer newApplications = oemApplicantRepo.countStatusNotChangedApplications(parentInfo.getLeft(), parentInfo.getRight(), true);
                return APPLICANT_MAPPER.toApplicantStatusCountDto(newApplications);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ApplicantListDto getApplicants(ApplicantSearchDto applicantSearchDto) {
        Account account = GeneralUtils.getCurrentUser();

        Pair<String, String> parentInfo;
        boolean hasPermission;

        if (account.getSubRole().equals(SubRole.OPERATOR.getId())) {
            OperatorClientAccount opParent = utilService.getOperatorParent(account);
            parentInfo = Pair.of(opParent.getId(), null);
            hasPermission = GeneralUtils.hasPermission(ScreenPermission.MESSAGE)
                    && opParent.getIsDomainEnabled();
        } else {
            OemClientAccount oemParent = utilService.getOemParent(account);
            parentInfo = Pair.of(oemParent.getId(), oemParent.getOemGroupId());
            hasPermission = GeneralUtils.hasPermission(ScreenPermission.MESSAGE)
                    && oemParent.getIsDomainEnabled();
        }

        Page<ApplicantSearchItemsDto> applicants = applicantCriteriaBuilder.findAllApplicantsByConditions(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                APPLICANT_MAPPER.toApplicantSearchBoxRequest(applicantSearchDto)
        );

        return ApplicantListDto.builder()
                .page(applicants.getNumber() + 1)
                .limit(applicants.getSize())
                .total(applicants.getTotalElements())
                .items(applicants.getContent().stream().map(i -> APPLICANT_MAPPER.toApplicantItemsDto(i, hasPermission))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ApplicantDetailsDto getApplicantDetails(String id) {
        Account account = GeneralUtils.getCurrentUser();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorApplicant applicant = getOperatorApplicantExistingById(id);
                ApplicantDetailsDto applicantDetailsDto = APPLICANT_MAPPER.toOperatorApplicantDetails(applicant);

                List<ApplicantInterviewDto> applicantInterviews = new ArrayList<>();
                for (OperatorApplicantInterview i : applicant.getInterviews()) {
                    ApplicantInterviewDto applicantInterview = APPLICANT_MAPPER.toOperatorApplicantInterviewDetails(i);
                    applicantInterviews.add(applicantInterview);
                }
                applicantDetailsDto.setInterviews(applicantInterviews);

                return applicantDetailsDto;
            }
            case SubRole.OEM -> {
                OemApplicant applicant = getOemApplicantExistingById(id);
                ApplicantDetailsDto applicantDetailsDto = APPLICANT_MAPPER.toOemApplicantDetails(applicant);

                List<ApplicantInterviewDto> applicantInterviews = new ArrayList<>();
                for (OemApplicantInterview i : applicant.getInterviews()) {
                    ApplicantInterviewDto applicantInterview = APPLICANT_MAPPER.toOemApplicantInterviewDetails(i);
                    applicantInterviews.add(applicantInterview);
                }
                applicantDetailsDto.setInterviews(applicantInterviews);

                return applicantDetailsDto;
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public MProfileDto getApplicantProfiles(String id) {
        Account account = GeneralUtils.getCurrentUser();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorApplicant applicant = getOperatorApplicantExistingById(id);
                MProfileDto profile = APPLICANT_MAPPER.toOperatorApplicantProfiles(applicant);
                updateApplicantProfileMasterData(
                        profile,
                        applicant.getQualificationIds(),
                        applicant.getExperienceIds(),
                        null,
                        applicant.getParent().getId());
                return profile;
            }
            case SubRole.OEM -> {
                OemApplicant applicant = getOemApplicantExistingById(id);
                MProfileDto profile = APPLICANT_MAPPER.toOemApplicantProfiles(applicant);
                updateApplicantProfileMasterData(
                        profile,
                        applicant.getQualificationIds(),
                        applicant.getExperienceIds(),
                        applicant.getOemGroupId(),
                        applicant.getParent().getId());
                return profile;
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void createNewApplicant(ApplicantUpsertDto applicantUpsertDto) {

        Account account = GeneralUtils.getCurrentUser();
        List<String> expIds = applicantUpsertDto.getExperienceIds();
        List<String> qualificationIds = applicantUpsertDto.getQualificationIds();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                SelectionStatus status = validateSelectionStatus(applicantUpsertDto, parent.getId(), null);

                OperatorApplicant newApplicant = APPLICANT_MAPPER.toOperatorApplicant(applicantUpsertDto);
                newApplicant.setParent(parent);
                newApplicant.setIsCrawledData(false);

                updateOperatorRelationships(newApplicant, applicantUpsertDto);
                updateOperatorApplicantSelectionStatus(status, newApplicant, false);
                updateOperatorApplicantMasterData(expIds, StatusType.EXPERIENCE, newApplicant);
                updateOperatorApplicantMasterData(qualificationIds, StatusType.QUALIFICATION, newApplicant);
                updateOperatorApplicantInterviews(newApplicant, applicantUpsertDto);
                utilService.updateApplicantBlacklistData(null, newApplicant);

                operatorApplicantRepo.save(newApplicant);
                operatorApplicantRepo.updateMailDuplicate();
                operatorApplicantRepo.updateTelDuplicate();
            }
            case SubRole.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                SelectionStatus status = validateSelectionStatus(applicantUpsertDto, parent.getId(), parent.getOemGroupId());

                OemApplicant newApplicant = APPLICANT_MAPPER.toOemApplicant(applicantUpsertDto);
                newApplicant.setParent(parent);
                newApplicant.setOemGroupId(parent.getOemGroupId());
                newApplicant.setIsCrawledData(false);

                updateOemRelationships(newApplicant, applicantUpsertDto);
                updateOemApplicantSelectionStatus(status, newApplicant, false);
                updateOemApplicantMasterData(expIds, StatusType.EXPERIENCE, newApplicant);
                updateOemApplicantMasterData(qualificationIds, StatusType.QUALIFICATION, newApplicant);
                updateOemApplicantInterviews(newApplicant, applicantUpsertDto);
                utilService.updateApplicantBlacklistData(newApplicant, null);

                oemApplicantRepo.save(newApplicant);
                oemApplicantRepo.updateMailDuplicate();
                oemApplicantRepo.updateTelDuplicate();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editApplicant(String id, ApplicantUpsertDto applicantUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        List<String> expIds = applicantUpsertDto.getExperienceIds();
        List<String> qualificationIds = applicantUpsertDto.getQualificationIds();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorApplicant applicant = getOperatorApplicantExistingById(id);
                SelectionStatus status = validateSelectionStatus(applicantUpsertDto, applicant.getParent().getId(), null);

                boolean isDuplicateChanged = !(Objects.equals(applicantUpsertDto.getFullName(), applicant.getFullName())
                        && Objects.equals(applicantUpsertDto.getTel(), applicant.getTel())
                        && Objects.equals(applicantUpsertDto.getEmail(), applicant.getEmail()));

                APPLICANT_MAPPER.updateToOperatorApplicant(applicantUpsertDto, applicant);

                updateOperatorRelationships(applicant, applicantUpsertDto);
                updateOperatorApplicantSelectionStatus(status, applicant, false);
                updateOperatorApplicantMasterData(expIds, StatusType.EXPERIENCE, applicant);
                updateOperatorApplicantMasterData(qualificationIds, StatusType.QUALIFICATION, applicant);
                updateOperatorApplicantInterviews(applicant, applicantUpsertDto);
                utilService.updateApplicantBlacklistData(null, applicant);

                operatorApplicantRepo.saveAndFlush(applicant);

                if (isDuplicateChanged) {
                    operatorApplicantRepo.updateMailDuplicate();
                    operatorApplicantRepo.updateTelDuplicate();
                }
            }
            case SubRole.OEM -> {
                OemApplicant applicant = getOemApplicantExistingById(id);
                SelectionStatus status = validateSelectionStatus(applicantUpsertDto, applicant.getParent().getId(), applicant.getOemGroupId());

                boolean isDuplicateChanged = !(Objects.equals(applicantUpsertDto.getFullName(), applicant.getFullName())
                        && Objects.equals(applicantUpsertDto.getTel(), applicant.getTel())
                        && Objects.equals(applicantUpsertDto.getEmail(), applicant.getEmail()));

                APPLICANT_MAPPER.updateToOemApplicant(applicantUpsertDto, applicant);

                updateOemRelationships(applicant, applicantUpsertDto);
                updateOemApplicantSelectionStatus(status, applicant, false);
                updateOemApplicantMasterData(expIds, StatusType.EXPERIENCE, applicant);
                updateOemApplicantMasterData(qualificationIds, StatusType.QUALIFICATION, applicant);
                updateOemApplicantInterviews(applicant, applicantUpsertDto);
                utilService.updateApplicantBlacklistData(applicant, null);

                oemApplicantRepo.saveAndFlush(applicant);

                if (isDuplicateChanged) {
                    oemApplicantRepo.updateMailDuplicate();
                    oemApplicantRepo.updateTelDuplicate();
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editApplicantMemo(String id, ApplicantMemoDto applicantMemoDto) {
        Account account = GeneralUtils.getCurrentUser();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorApplicant operatorApplicant = getOperatorApplicantExistingById(id);
                operatorApplicant.setMemo(applicantMemoDto.getMemo());
                operatorApplicantRepo.saveAndFlush(operatorApplicant);
            }
            case SubRole.OEM -> {
                OemApplicant oemApplicant = getOemApplicantExistingById(id);
                oemApplicant.setMemo(applicantMemoDto.getMemo());
                oemApplicantRepo.saveAndFlush(oemApplicant);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editApplicantSelectionStatus(String id, ApplicantStatusUpsertDto applicantStatusUpsertDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        SelectionStatus status = null;
        if (applicantStatusUpsertDto.getStatusId() != null && !applicantStatusUpsertDto.getStatusId().isEmpty()) {
            status = getApplicantSelectionStatus(applicantStatusUpsertDto.getStatusId(), parentInfo.getLeft(), parentInfo.getRight());
        }

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorApplicant operatorApplicant = getOperatorApplicantExistingById(id);
                updateOperatorApplicantSelectionStatus(status, operatorApplicant, true);
                operatorApplicantRepo.saveAndFlush(operatorApplicant);
            }
            case SubRole.OEM -> {
                OemApplicant oemApplicant = getOemApplicantExistingById(id);
                updateOemApplicantSelectionStatus(status, oemApplicant, true);
                oemApplicantRepo.saveAndFlush(oemApplicant);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void editApplicantProject(String id, ApplicantProjectDto applicantProjectDto) {
        Account account = GeneralUtils.getCurrentUser();
        String projectId = applicantProjectDto.getProjectId();
        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorProject project = utilService.getOperatorProjectExistingById(projectId);
                OperatorApplicant operatorApplicant = getOperatorApplicantExistingById(id);
                operatorApplicant.setProject(project);
                operatorApplicantRepo.saveAndFlush(operatorApplicant);
            }
            case SubRole.OEM -> {
                OemProject project = utilService.getOemProjectExistingById(projectId);
                OemApplicant oemApplicant = getOemApplicantExistingById(id);
                oemApplicant.setProject(project);
                oemApplicantRepo.saveAndFlush(oemApplicant);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public ProjectApplicantListDto getApplicationListBasedOnFlowType(String projectId, String flowId, Integer pageNumber, Integer pageSize, String arrangedBy) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        Pageable pageable = GeneralUtils.getPagination(
                pageNumber,
                pageSize,
                getSortBy(arrangedBy));

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                Page<OperatorApplicant> applicants = operatorApplicantRepo.findAllApplicantsByFlowTypeForProject(
                        projectId,
                        flowId,
                        parentInfo.getLeft(),
                        pageable
                );
                return ProjectApplicantListDto.builder()
                        .total(applicants.getTotalElements())
                        .page(applicants.getNumber() + 1)
                        .limit(applicants.getSize())
                        .items(applicants.stream().map(APPLICANT_MAPPER::toProjectApplicantsDto).toList())
                        .build();
            }
            case SubRole.OEM -> {
                Page<OemApplicant> applicants = oemApplicantRepo.findAllApplicantsByFlowTypeForProject(
                        projectId,
                        flowId,
                        parentInfo.getLeft(),
                        parentInfo.getRight(),
                        pageable
                );
                return ProjectApplicantListDto.builder()
                        .total(applicants.getTotalElements())
                        .page(applicants.getNumber() + 1)
                        .limit(applicants.getSize())
                        .items(applicants.stream().map(APPLICANT_MAPPER::toProjectApplicantsDto).toList())
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private void updateOperatorApplicantInterviews(OperatorApplicant applicant, ApplicantUpsertDto applicantUpsertDto) {
        if (Objects.nonNull(applicantUpsertDto.getInterviews())) {
            applicant.removeEmptyInterviewsById(
                    applicantUpsertDto.getInterviews()
                            .stream().map(ApplicantInterviewUpsertDto::getId)
                            .collect(Collectors.toList())
            );
            for (ApplicantInterviewUpsertDto i : applicantUpsertDto.getInterviews()) {
                OperatorApplicantInterview applicantInterview = APPLICANT_MAPPER.toOperatorApplicantInterview(i);
                applicantInterview.setCategory(utilService.findInterviewCategoryById(i.getCategoryId()));
                applicantInterview.setParentId(applicant.getParent().getId());

                if (Objects.isNull(i.getId())) {
                    applicant.addToInterviews(applicantInterview);
                } else {
                    applicant.editInterview(applicantInterview);
                }
            }
        } else {
            applicant.setInterviews(null);
        }
    }

    private void updateOemApplicantInterviews(OemApplicant applicant, ApplicantUpsertDto applicantUpsertDto) {
        if (Objects.nonNull(applicantUpsertDto.getInterviews())) {
            applicant.removeEmptyInterviewsById(
                    applicantUpsertDto.getInterviews()
                            .stream().map(ApplicantInterviewUpsertDto::getId)
                            .collect(Collectors.toList())
            );
            for (ApplicantInterviewUpsertDto i : applicantUpsertDto.getInterviews()) {
                OemApplicantInterview applicantInterview = APPLICANT_MAPPER.toOemApplicantInterview(i);
                applicantInterview.setCategory(utilService.findInterviewCategoryById(i.getCategoryId()));
                applicantInterview.setParentId(applicant.getParent().getId());
                applicantInterview.setOemGroupId(applicant.getOemGroupId());

                if (Objects.isNull(i.getId())) {
                    applicant.addToInterviews(applicantInterview);
                } else {
                    applicant.editInterview(applicantInterview);
                }
            }
        } else {
            applicant.setInterviews(null);
        }
    }

    private void updateOperatorRelationships(OperatorApplicant applicant, ApplicantUpsertDto applicantUpsertDto) {
        // Gender
        if (Objects.nonNull(applicantUpsertDto.getGender())) {
            Gender gender = Gender.fromId(applicantUpsertDto.getGender());
            applicant.setGender(gender.getId());
        } else {
            applicant.setGender(null);
        }

        // Occupation
        if (Objects.nonNull(applicantUpsertDto.getOccupation())) {
            OccupationType occupation = OccupationType.fromId(applicantUpsertDto.getOccupation());
            applicant.setOccupation(occupation.getId());
        } else {
            applicant.setOccupation(null);
        }

        // Prefecture
        Prefecture prefecture = utilService.findPrefecture(applicantUpsertDto.getPrefecture());
        applicant.setPrefecture(prefecture);

        if (Objects.nonNull(applicantUpsertDto.getGender())) {
            Gender gender = Gender.fromId(applicantUpsertDto.getGender());
            applicant.setGender(gender.getId());
        } else {
            applicant.setGender(null);
        }

        // Project
        if (Objects.nonNull(applicantUpsertDto.getProjectId())) {
            OperatorProject project = utilService.getOperatorProjectExistingById(applicantUpsertDto.getProjectId());
            applicant.setProject(project);
        } else {
            applicant.setProject(null);
        }

        // Pic
        if (Objects.nonNull(applicantUpsertDto.getPicId())) {
            OperatorClientAccount manager = utilService.getExistingOperatorClientAccountById(applicantUpsertDto.getPicId());
            applicant.setPic(manager);
        } else {
            applicant.setPic(null);
        }

        // Media
        if (!applicant.getIsCrawledData()) {
            applicant.setMedia(
                    Objects.nonNull(applicantUpsertDto.getMediaId()) ? findMasterMedia(applicantUpsertDto.getMediaId())
                            : null);
        }
    }

    private void updateOemRelationships(OemApplicant applicant, ApplicantUpsertDto applicantUpsertDto) {
        // Gender
        if (Objects.nonNull(applicantUpsertDto.getGender())) {
            Gender gender = Gender.fromId(applicantUpsertDto.getGender());
            applicant.setGender(gender.getId());
        } else {
            applicant.setGender(null);
        }

        // Occupation
        if (Objects.nonNull(applicantUpsertDto.getOccupation())) {
            OccupationType occupation = OccupationType.fromId(applicantUpsertDto.getOccupation());
            applicant.setOccupation(occupation.getId());
        } else {
            applicant.setOccupation(null);
        }

        // Prefecture
        Prefecture prefecture = utilService.findPrefecture(applicantUpsertDto.getPrefecture());
        applicant.setPrefecture(prefecture);

        if (Objects.nonNull(applicantUpsertDto.getGender())) {
            Gender gender = Gender.fromId(applicantUpsertDto.getGender());
            applicant.setGender(gender.getId());
        } else {
            applicant.setGender(null);
        }

        // Project
        if (Objects.nonNull(applicantUpsertDto.getProjectId())) {
            OemProject project = utilService.getOemProjectExistingById(applicantUpsertDto.getProjectId());
            applicant.setProject(project);
        } else {
            applicant.setProject(null);
        }

        // Pic
        if (Objects.nonNull(applicantUpsertDto.getPicId())) {
            OemClientAccount manager = utilService.getExistingOemClientAccountById(applicantUpsertDto.getPicId());
            applicant.setPic(manager);
        } else {
            applicant.setPic(null);
        }

        // Media
        if (!applicant.getIsCrawledData()) {
            applicant.setMedia(
                    Objects.nonNull(applicantUpsertDto.getMediaId()) ? findMasterMedia(applicantUpsertDto.getMediaId())
                            : null);
        }
    }

    private void updateOperatorApplicantMasterData(List<String> ids, StatusType type, OperatorApplicant operatorApplicant) {
        if (Objects.requireNonNull(type) == StatusType.QUALIFICATION) {
            if (Objects.nonNull(ids) && !ids.isEmpty()) {
                List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                        ids,
                        null,
                        operatorApplicant.getParent().getId(),
                        StatusType.QUALIFICATION
                );
                String qualificationString = statuses.stream()
                        .map(status -> String.valueOf(status.getId()))
                        .collect(Collectors.joining(","));
                operatorApplicant.setQualificationIds(qualificationString);
            } else {
                operatorApplicant.setQualificationIds(null);
            }
        } else if (Objects.requireNonNull(type) == StatusType.EXPERIENCE) {
            if (Objects.nonNull(ids) && !ids.isEmpty()) {
                List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                        ids,
                        null,
                        operatorApplicant.getParent().getId(),
                        StatusType.EXPERIENCE
                );
                String experienceString = statuses.stream()
                        .map(status -> String.valueOf(status.getId()))
                        .collect(Collectors.joining(","));
                operatorApplicant.setExperienceIds(experienceString);
            } else {
                operatorApplicant.setExperienceIds(null);
            }
        }
    }

    private void updateOemApplicantMasterData(List<String> ids, StatusType type, OemApplicant oemApplicant) {
        if (Objects.requireNonNull(type) == StatusType.QUALIFICATION) {
            if (Objects.nonNull(ids) && !ids.isEmpty()) {
                List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                        ids,
                        oemApplicant.getOemGroupId(),
                        oemApplicant.getParent().getId(),
                        StatusType.QUALIFICATION
                );
                String qualificationString = statuses.stream()
                        .map(status -> String.valueOf(status.getId()))
                        .collect(Collectors.joining(","));
                oemApplicant.setQualificationIds(qualificationString);
            } else {
                oemApplicant.setQualificationIds(null);
            }
        } else if (Objects.requireNonNull(type) == StatusType.EXPERIENCE) {
            if (Objects.nonNull(ids) && !ids.isEmpty()) {
                List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                        ids,
                        oemApplicant.getOemGroupId(),
                        oemApplicant.getParent().getId(),
                        StatusType.EXPERIENCE
                );
                String experienceString = statuses.stream()
                        .map(status -> String.valueOf(status.getId()))
                        .collect(Collectors.joining(","));
                oemApplicant.setExperienceIds(experienceString);
            } else {
                oemApplicant.setExperienceIds(null);
            }
        }
    }

    private void updateApplicantProfileMasterData(MProfileDto mProfileDto, String qualificationIds, String experienceIds, String oemGroupId, String parentId) {
        if (Objects.nonNull(qualificationIds) && !qualificationIds.isEmpty()) {
            List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                    Arrays.stream(qualificationIds.split(",")).toList(),
                    oemGroupId,
                    parentId,
                    StatusType.QUALIFICATION
            );
            List<String> qualifications = statuses.stream()
                    .map(MasterStatus::getStatusName)
                    .collect(Collectors.toList());
            mProfileDto.setQualifications(qualifications);
        } else {
            mProfileDto.setQualifications(null);
        }
        if (Objects.nonNull(experienceIds) && !experienceIds.isEmpty()) {
            List<MasterStatus> statuses = utilService.findMasterStatusesByType(
                    Arrays.stream(experienceIds.split(",")).toList(),
                    oemGroupId,
                    parentId,
                    StatusType.EXPERIENCE
            );
            List<String> experiences = statuses.stream()
                    .map(MasterStatus::getStatusName)
                    .collect(Collectors.toList());
            mProfileDto.setExperiences(experiences);
        } else {
            mProfileDto.setExperiences(null);
        }
    }

    @Transactional
    public void deleteSelectedApplicants(SelectedIds selectedIds) {
        Account account = GeneralUtils.getCurrentUser();
        List<String> applicantIds = selectedIds.getSelectedIds();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                
                // Validate that all applicants exist and belong to the current user
                List<OperatorApplicant> existingApplicants = operatorApplicantRepo.findAllApplicantsByIds(applicantIds, parent.getId());
                if (existingApplicants.size() != applicantIds.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(ErrorMessage.INVALID_DATA.getMessage())
                            .fieldError("selectedIds")
                            .build());
                }
                
                // Delete the applicants
                operatorApplicantRepo.deleteAll(existingApplicants);

                operatorApplicantRepo.updateMailDuplicate();
                operatorApplicantRepo.updateTelDuplicate();
            }
            case SubRole.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                
                // Validate that all applicants exist and belong to the current user
                List<OemApplicant> existingApplicants = oemApplicantRepo.findAllApplicantsByIds(applicantIds, parent.getId(), parent.getOemGroupId());
                if (existingApplicants.size() != applicantIds.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(ErrorMessage.INVALID_DATA.getMessage())
                            .fieldError("selectedIds")
                            .build());
                }
                
                // Delete the applicants
                oemApplicantRepo.deleteAll(existingApplicants);

                oemApplicantRepo.updateMailDuplicate();
                oemApplicantRepo.updateTelDuplicate();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    private void updateOperatorApplicantSelectionStatus(SelectionStatus status, OperatorApplicant operatorApplicant, boolean isStatusEdit) {
        // Key in FE: empty string
        if (Objects.nonNull(status)) {
            if (Objects.isNull(operatorApplicant.getSelectionStatus()) ||
                    !Objects.equals(status, operatorApplicant.getSelectionStatus())) {
                operatorApplicant.setLstStatusChangeDateTime(LocalDateTime.now());

                if (isStatusEdit && Objects.equals(status.getFlowType(), FlowType.AGREEMENT.getId()) && (
                            Objects.isNull(operatorApplicant.getSelectionStatus()) ||
                            !Objects.equals(operatorApplicant.getSelectionStatus().getFlowType(), FlowType.AGREEMENT.getId()) ||
                            (Objects.equals(operatorApplicant.getSelectionStatus().getFlowType(), FlowType.AGREEMENT.getId()) &&
                                    Objects.isNull(operatorApplicant.getHiredDate()))
                        )
                ) {
                    operatorApplicant.setHiredDate(LocalDate.now());
                }

                operatorApplicant.setSelectionStatus(status);
            }
        } else {
            if (Objects.nonNull(operatorApplicant.getSelectionStatus())) {
                operatorApplicant.setLstStatusChangeDateTime(LocalDateTime.now());
            }

            operatorApplicant.setSelectionStatus(null);
        }
    }

    private void updateOemApplicantSelectionStatus(SelectionStatus status, OemApplicant oemApplicant, boolean isStatusEdit) {
        if (Objects.nonNull(status)) {
            if (Objects.isNull(oemApplicant.getSelectionStatus()) ||
                    !Objects.equals(status, oemApplicant.getSelectionStatus())) {
                oemApplicant.setLstStatusChangeDateTime(LocalDateTime.now());

                if (isStatusEdit && Objects.equals(status.getFlowType(), FlowType.AGREEMENT.getId()) && (
                        Objects.isNull(oemApplicant.getSelectionStatus()) ||
                        !Objects.equals(oemApplicant.getSelectionStatus().getFlowType(), FlowType.AGREEMENT.getId()) ||
                        (Objects.equals(oemApplicant.getSelectionStatus().getFlowType(), FlowType.AGREEMENT.getId()) &&
                                Objects.isNull(oemApplicant.getHiredDate()))
                    )
                ) {
                    oemApplicant.setHiredDate(LocalDate.now());
                }

                oemApplicant.setSelectionStatus(status);
            }
        } else {
            if (Objects.nonNull(oemApplicant.getSelectionStatus())) {
                oemApplicant.setLstStatusChangeDateTime(LocalDateTime.now());
            }

            oemApplicant.setSelectionStatus(null);
        }
    }

    public OperatorApplicant getOperatorApplicantExistingById(String id) {
        Optional<OperatorApplicant> optionalOne = operatorApplicantRepo.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    public OemApplicant getOemApplicantExistingById(String id) {
        Optional<OemApplicant> optionalOne = oemApplicantRepo.findById(id);

        if (optionalOne.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT.getTargetName()))
                    .fieldError("id")
                    .build());
        }

        return optionalOne.get();
    }

    private SelectionStatus getApplicantSelectionStatus(String id, String parentId, String oemGroupId) {
        Optional<SelectionStatus> status = selectionStatusRepo.findStatusBy(
                id,
                oemGroupId,
                parentId
        );

        if (status.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.NOT_FOUND.getStatusCode())
                    .message(String.format(
                            ErrorMessage.NOT_FOUND.getMessage(),
                            TargetName.APPLICANT_MASTER_DATA.getTargetName()))
                    .fieldError("ids")
                    .build());
        }

        return status.get();
    }

    private Sort getSortBy(String arrangedBy) {
        if (arrangedBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] arrangedByArray = arrangedBy.split(":");
        if (arrangedByArray.length != 2 || !Constants.PROJECT_APPLICANT_ARRANGED_BY.containsKey(arrangedByArray[0]) ||
                !Constants.SORT_ORDERS.containsKey(arrangedByArray[1])) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_OPERATOR.getStatusCode())
                    .message(ErrorMessage.INVALID_OPERATOR.getMessage())
                    .fieldError("arrangedBy")
                    .build());
        }

        return Sort.by(Sort.Direction.fromString(Constants.SORT_ORDERS.get(arrangedByArray[1])),
                Constants.PROJECT_APPLICANT_ARRANGED_BY.get(arrangedByArray[0]));
    }

    private MasterMedia findMasterMedia(String mediaId) {
        Optional<MasterMedia> masterMedia = masterMediaRepository.findById(mediaId);
        if (masterMedia.isEmpty()) {
            throw new NotFoundException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(
                            ErrorMessage.INVALID_DATA.getMessage(),
                            TargetName.MASTER_MEDIA.getTargetName()))
                    .fieldError("mediaId")
                    .build());
        }
        return masterMedia.get();
    }

    private SelectionStatus validateSelectionStatus(ApplicantUpsertDto applicantUpsertDto, String parentId, String oemGroupId) {
        SelectionStatus status = null;
        if (Objects.nonNull(applicantUpsertDto.getSelectionStatusId())) {
            status = getApplicantSelectionStatus(applicantUpsertDto.getSelectionStatusId(), parentId, oemGroupId);

            if (Objects.equals(status.getFlowType(), FlowType.AGREEMENT.getId()) && Objects.isNull(applicantUpsertDto.getJoinDate())) {
                throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.REQUIRED_FIELD.getStatusCode())
                    .message(String.format(ErrorMessage.REQUIRED_FIELD.getMessage(),
                            "入社日"))
                    .fieldError("joinDate")
                    .build());
            }
        }

        return status;
    }
}