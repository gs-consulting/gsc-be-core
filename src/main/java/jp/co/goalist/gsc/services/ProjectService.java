package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.ProjectInfoByFlow;
import jp.co.goalist.gsc.dtos.ProjectPermissionsDto;
import jp.co.goalist.gsc.dtos.ProjectSearchBoxRequest;
import jp.co.goalist.gsc.dtos.ProjectSearchItemsDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantTotalCountDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.*;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.criteriaBuilder.ProjectCriteriaBuilder;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.AdvertisementMapper.ADVERTISEMENT_MAPPER;
import static jp.co.goalist.gsc.mappers.ProjectMapper.PROJECT_MAPPER;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final OemProjectRepository oemProjectRepository;
    private final OperatorProjectRepository operatorProjectRepository;
    private final OemAdvertisementRepository oemAdvertisementRepo;
    private final OperatorAdvertisementRepository operatorAdvertisementRepo;
    private final OemApplicantRepository oemApplicantRepo;
    private final OperatorApplicantRepository operatorApplicantRepo;
    private final ApplicantService applicantService;
    private final UtilService utilService;
    private final ProjectCriteriaBuilder projectCriteriaBuilder;

    @Transactional
    public ProjectAdvertListDto getProjectAdvertisements(String id) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        List<ProjectAdvertItemsDto> advertItemsDto = new ArrayList<>();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorProject operatorProject = utilService.getOperatorProjectExistingById(id);
                Page<OperatorAdvertisement> advertisements = operatorAdvertisementRepo.findLinkedAdvertisements(
                        operatorProject.getId(),
                        parentInfo.getLeft(),
                        GeneralUtils.getPagination(null, null));
                advertItemsDto.addAll(advertisements.getContent().stream()
                        .map(ADVERTISEMENT_MAPPER::toProjectAdvertisements).toList());
            }
            case SubRole.OEM -> {
                OemProject oemProject = utilService.getOemProjectExistingById(id);
                Page<OemAdvertisement> advertisements = oemAdvertisementRepo.findLinkedAdvertisements(
                        oemProject.getId(),
                        parentInfo.getLeft(),
                        GeneralUtils.getPagination(null, null));
                advertItemsDto.addAll(advertisements.getContent().stream()
                        .map(ADVERTISEMENT_MAPPER::toProjectAdvertisements).toList());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        long total = advertItemsDto.stream()
                .mapToLong(i -> Objects.nonNull(i.getAmount()) ? i.getAmount() : 0)
                .sum();

        return ProjectAdvertListDto.builder()
                .total(total)
                .items(advertItemsDto)
                .build();
    }

    @Transactional
    public ProjectListDto getProjectList(ProjectSearchBoxDto projectSearchBoxDto) {
        Account account = GeneralUtils.getCurrentUser();
        boolean hasPermission = GeneralUtils.hasPermission(ScreenPermission.PROJECT);
        ProjectSearchBoxRequest request = PROJECT_MAPPER.toProjectSearchBoxRequest(projectSearchBoxDto);
        SubRole subRole = SubRole.fromId(account.getSubRole());
        List<ApplicantTotalCountDto> applicationStatistics;
        boolean hasProjectPermission = false;

        switch (subRole) {
            case SubRole.OPERATOR -> {
                OperatorClientAccount clientAccount = utilService.getExistingOperatorClientAccountById(account.getId());
                request.setEmploymentType(clientAccount.getEmploymentType());
                request.setParentId(Objects.nonNull(clientAccount.getParent()) ? clientAccount.getParent().getId() : clientAccount.getId());

                applicationStatistics = operatorApplicantRepo.countAllApplicantsForProject(request.getParentId());
                if (Objects.nonNull(clientAccount.getPermissions()) && clientAccount.getPermissions().contains(ScreenPermission.PROJECT.getId())) {
                    hasProjectPermission = true;
                }
            }
            case SubRole.OEM -> {
                OemClientAccount clientAccount = utilService.getExistingOemClientAccountById(account.getId());
                request.setEmploymentType(clientAccount.getEmploymentType());
                request.setParentId(Objects.nonNull(clientAccount.getParent()) ? clientAccount.getParent().getId() : clientAccount.getId());
                request.setOemGroupId(clientAccount.getOemGroupId());

                applicationStatistics = oemApplicantRepo.countAllApplicantsForProject(request.getParentId(), request.getOemGroupId());
                if (Objects.nonNull(clientAccount.getPermissions()) && clientAccount.getPermissions().contains(ScreenPermission.PROJECT.getId())) {
                    hasProjectPermission = true;
                }
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        Page<ProjectSearchItemsDto> projects = projectCriteriaBuilder.findAllProjectsByConditions(request);
        Map<String, ApplicantTotalCountDto> applicationStatisticMap = applicationStatistics.stream()
                .collect(Collectors.toMap(ApplicantTotalCountDto::getCountId, Function.identity()));

        boolean finalHasProjectPermission = hasProjectPermission;
        return ProjectListDto.builder()
                .page(projects.getNumber() + 1)
                .limit(projects.getSize())
                .total(projects.getTotalElements())
                .items(projects.getContent().stream().map(i ->
                        PROJECT_MAPPER.toProjectItemsDto(i,
                                finalHasProjectPermission,
                                applicationStatisticMap.get(i.getId()))
                ).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ProjectApplicantsDto getApplicationListBasedOnFlowType(String id, String flowType, Integer pageNumber, Integer pageSize, String arrangedBy) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                ProjectInfoByFlow project = operatorProjectRepository.findProjectById(id, parentInfo.getLeft());
                ApplicantTotalCountDto applicationStatistics = operatorApplicantRepo.countApplicantsForProjectById(parentInfo.getLeft(), id);

                ProjectInfoDto projectInfo = PROJECT_MAPPER.toProjectInfoDto(project, applicationStatistics);
                projectInfo.setFlowType(flowType);
                return ProjectApplicantsDto.builder()
                        .project(projectInfo)
                        .applicants(applicantService.getApplicationListBasedOnFlowType(project.getId(), flowType, pageNumber, pageSize, arrangedBy))
                        .build();
            }
            case SubRole.OEM -> {
                ProjectInfoByFlow project = oemProjectRepository.findProjectById(id, parentInfo.getLeft(), parentInfo.getRight());
                ApplicantTotalCountDto applicationStatistics = oemApplicantRepo.countApplicantsForProjectById(parentInfo.getLeft(), parentInfo.getRight(), id);

                ProjectInfoDto projectInfo = PROJECT_MAPPER.toProjectInfoDto(project, applicationStatistics);
                projectInfo.setFlowType(flowType);
                return ProjectApplicantsDto.builder()
                        .project(projectInfo)
                        .applicants(applicantService.getApplicationListBasedOnFlowType(project.getId(), flowType, pageNumber, pageSize, arrangedBy))
                        .build();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public void saveProjectBranchPermissionSetting(String id, ProjectPermissionUpsertDto form) {
        Account account = GeneralUtils.getCurrentUser();
        List<String> ids = form.getItems().stream().map(ProjectPermissionUpsertItemsDto::getId).toList();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                List<OperatorProject> projects = operatorProjectRepository.findAllProjectsByIdsAndBranchId(
                        ids,
                        parent.getId(),
                        id
                );

                if (projects.size() != ids.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    TargetName.PROJECT.getTargetName()))
                            .fieldError("id")
                            .build());
                }

                projectCriteriaBuilder.updateForProjectPermissions(PROJECT_MAPPER.toProjectPermissionDto(form.getItems()), true);
            }
            case SubRole.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                List<OemProject> projects = oemProjectRepository.findAllProjectsByIdsAndBranchId(
                        ids,
                        parent.getId(),
                        id
                );

                if (projects.size() != ids.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    TargetName.PROJECT.getTargetName()))
                            .fieldError("id")
                            .build());
                }

                projectCriteriaBuilder.updateForProjectPermissions(PROJECT_MAPPER.toProjectPermissionDto(form.getItems()), false);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public void saveProjectNoBranchPermissionSetting(List<ClientBranchRestrictionUpsertDto> form) {
        Account account = GeneralUtils.getCurrentUser();
        List<String> ids = form.stream().map(ClientBranchRestrictionUpsertDto::getId).toList();

        switch (SubRole.fromId(account.getSubRole())) {
            case SubRole.OPERATOR -> {
                OperatorClientAccount parent = utilService.getOperatorParent(account);
                List<OperatorProject> projects = operatorProjectRepository.findAllProjectsByIdsAndBranchId(
                        ids,
                        parent.getId(),
                        null
                );

                if (projects.size() != ids.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    TargetName.PROJECT.getTargetName()))
                            .fieldError("id")
                            .build());
                }

                projectCriteriaBuilder.updateForProjectPermissions(PROJECT_MAPPER.toBranchProjectPermissionDto(form), true);
            }
            case SubRole.OEM -> {
                OemClientAccount parent = utilService.getOemParent(account);
                List<OemProject> projects = oemProjectRepository.findAllProjectsByIdsAndBranchId(
                        ids,
                        parent.getId(),
                        null
                );

                if (projects.size() != ids.size()) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    TargetName.PROJECT.getTargetName()))
                            .fieldError("id")
                            .build());
                }

                projectCriteriaBuilder.updateForProjectPermissions(PROJECT_MAPPER.toBranchProjectPermissionDto(form), false);
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    public List<ProjectPermissionItemsDto> getProjectListForBranchPermission(String id, ProjectSearchBoxDto projectSearchBoxDto) {
        List<ProjectPermissionsDto> items = getProjectPermissionItems(id, projectSearchBoxDto);
        return items.stream().map(PROJECT_MAPPER::toProjectPermissionsDto).toList();
    }

    public List<ClientBranchDataItemsDto> getClientBranchRelatedData() {
        List<ProjectPermissionsDto> items = getProjectPermissionItems(null, new ProjectSearchBoxDto());
        return items.stream().map(PROJECT_MAPPER::toBranchProjectPermissionsDto).toList();
    }

    private List<ProjectPermissionsDto> getProjectPermissionItems(String branchId, ProjectSearchBoxDto projectSearchBoxDto) {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        return projectCriteriaBuilder.findAllProjectsForPermission(
                parentInfo.getLeft(),
                parentInfo.getRight(),
                branchId,
                projectSearchBoxDto
        );
    }
}
