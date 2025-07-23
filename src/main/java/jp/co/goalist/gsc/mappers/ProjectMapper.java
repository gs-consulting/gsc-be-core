package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.dtos.*;
import jp.co.goalist.gsc.dtos.applicant.ApplicantTotalCountDto;
import jp.co.goalist.gsc.entities.Notification;
import jp.co.goalist.gsc.entities.OemProject;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProjectMapper extends BaseMapper {

    ProjectMapper PROJECT_MAPPER = Mappers.getMapper(ProjectMapper.class);

    @Mapping(target = "isGroupedByOccupation", expression = "java(projectSearchItems.getIsGroupedByOccupation() == 1)")
    @Mapping(target = "isGroupedByBranch", expression = "java(projectSearchItems.getIsGroupedByBranch() == 1)")
    @Mapping(target = "qualifications", expression = "java(convertQualifications(projectSearchItems.getQualifications()))")
    @Mapping(target = "registeredDate", source = "projectSearchItems.registeredDate", dateFormat = Constants.DATE_TIME_FORMAT)
    @Mapping(target = "statusId", source = "projectSearchItems.statusId")
    @Mapping(target = "hasPermission", source = "hasPermission")
    @Mapping(target = "totalApplies", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllApply() : null)")
    @Mapping(target = "totalInterviews", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllInterview() : null)")
    @Mapping(target = "totalOffers", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllOffer() : null)")
    @Mapping(target = "totalAgreements", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllAgreement() : null)")
    ProjectItemsDto toProjectItemsDto(ProjectSearchItemsDto projectSearchItems, boolean hasPermission, ApplicantTotalCountDto applicationStatistics);

    ProjectPermissionItemsDto toProjectPermissionsDto(ProjectPermissionsDto item);

    ClientBranchDataItemsDto toBranchProjectPermissionsDto(ProjectPermissionsDto item);

    @Mapping(target = "registeredDate", source = "projectSearchItems.registeredDate", dateFormat = Constants.DATE_TIME_FORMAT)
    @Mapping(target = "totalApplies", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllApply() : null)")
    @Mapping(target = "totalInterviews", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllInterview() : null)")
    @Mapping(target = "totalOffers", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllOffer() : null)")
    @Mapping(target = "totalAgreements", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllAgreement() : null)")
    DProjectItemsDto toDProjectItemsDto(ProjectSearchItemsDto projectSearchItems, ApplicantTotalCountDto applicationStatistics);

    @Mapping(target = "postingStartDate", source = "postingStartDate", dateFormat = Constants.DATE_FORMAT)
    @Mapping(target = "title", source = "title")
    DNotificationItemsDto toDNotificationItemsDto(Notification notification);

    @Mapping(target = "postingStartDate", source = "postingStartDate", dateFormat = Constants.DATE_FORMAT)
    DNotificationDetailsDto toDNotificationDetailsDto(Notification notification);

    List<ProjectPermissionUpsertMappingDto> toProjectPermissionDto(List<ProjectPermissionUpsertItemsDto> list);

    List<ProjectPermissionUpsertMappingDto> toBranchProjectPermissionDto(List<ClientBranchRestrictionUpsertDto> list);

    ProjectSearchBoxRequest toProjectSearchBoxRequest(ProjectSearchBoxDto projectSearchBoxDto);

    @Mapping(target = "qualifications", expression = "java(convertQualifications(project.getQualifications()))")
    @Mapping(target = "registeredDate", source = "project.registeredDate", dateFormat = Constants.DATE_TIME_FORMAT)
    @Mapping(target = "totalApplies", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllApply() : null)")
    @Mapping(target = "totalInterviews", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllInterview() : null)")
    @Mapping(target = "totalOffers", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllOffer() : null)")
    @Mapping(target = "totalAgreements", expression = "java(applicationStatistics != null ? (int) applicationStatistics.getAllAgreement() : null)")
    @Mapping(target = "flowType", ignore = true)
    ProjectInfoDto toProjectInfoDto(ProjectInfoByFlow project, ApplicantTotalCountDto applicationStatistics);

    default List<String> convertQualifications(String qualification) {
        return Objects.nonNull(qualification) ? Arrays.stream(qualification.split(",")).toList() : null;
    }
}
