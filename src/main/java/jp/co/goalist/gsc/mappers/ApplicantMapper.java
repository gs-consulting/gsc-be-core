package jp.co.goalist.gsc.mappers;

import jp.co.goalist.gsc.common.Constants;
import jp.co.goalist.gsc.dtos.applicant.ApplicantDashboardCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantSearchBoxRequest;
import jp.co.goalist.gsc.dtos.applicant.ApplicantSearchItemsDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantWithLatestMessagesDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Gender;
import jp.co.goalist.gsc.enums.OccupationType;
import jp.co.goalist.gsc.gen.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ApplicantMapper extends BaseMapper {

    ApplicantMapper APPLICANT_MAPPER = Mappers.getMapper(ApplicantMapper.class);

    ApplicantStatusCountDto toApplicantStatusCountDto(Integer count);

    ApplicantSearchBoxRequest toApplicantSearchBoxRequest(ApplicantSearchDto applicantSearchDto);

    @Mapping(target = "today", expression = "java(getTodayStatistic(statisticData))")
    @Mapping(target = "recent", expression = "java(getRecentStatistic(statisticData))")
    StatisticsListDto toStatisticsListDto(ApplicantDashboardCountDto statisticData);

    @Mapping(target = "registeredDateTime", source = "applicantSearchItemsDto.registeredDateTime", dateFormat = Constants.DATE_TIME_FORMAT)
    @Mapping(target = "latestContactDateTime", source = "applicantSearchItemsDto.latestContactDateTime", dateFormat = Constants.DATE_TIME_FORMAT)
    @Mapping(target = "interviewDateTime", source = "applicantSearchItemsDto.interviewDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "isUnread", expression = "java(applicantSearchItemsDto.getIsUnread())")
    @Mapping(target = "selectionStatusId", source = "applicantSearchItemsDto.selectionStatusId")
    @Mapping(target = "hasTel", expression = "java(applicantSearchItemsDto.getHasTel() == 1)")
    @Mapping(target = "hasEmail", expression = "java(applicantSearchItemsDto.getHasEmail() == 1)")
    @Mapping(target = "isDeletable", expression = "java(applicantSearchItemsDto.getIsDeletable() == 1)")
    @Mapping(target = "isValid", expression = "java(applicantSearchItemsDto.getIsValid() == 1)")
    @Mapping(target = "isBlacklist", expression = "java(applicantSearchItemsDto.getIsBlacklist() == 1)")
    @Mapping(target = "isStatusNotChanged", expression = "java(applicantSearchItemsDto.getIsStatusNotChanged() == 1)")
    @Mapping(target = "isDuplicate", expression = "java(applicantSearchItemsDto.getIsDuplicate() == 1)")
    @Mapping(target = "hasPermission", source = "hasPermission")
    ApplicantItemsDto toApplicantItemsDto(ApplicantSearchItemsDto applicantSearchItemsDto, boolean hasPermission);

    @Mapping(target = "prefecture", expression = "java(getPrefecture(oemApplicant.getPrefecture()))")
    @Mapping(target = "selectionStatusId", expression = "java(oemApplicant.getSelectionStatus() != null ? oemApplicant.getSelectionStatus().getId().toString() : null)")
    @Mapping(target = "qualificationIds", expression = "java(getQualificationIds(oemApplicant.getQualificationIds()))")
    @Mapping(target = "experienceIds", expression = "java(getExperienceIds(oemApplicant.getExperienceIds()))")
    @Mapping(target = "picId", expression = "java(oemApplicant.getPic() != null ? oemApplicant.getPic().getId() : null)")
    @Mapping(target = "projectId", expression = "java(oemApplicant.getProject() != null ? oemApplicant.getProject().getId() : null)")
    @Mapping(target = "mediaId", expression = "java(oemApplicant.getMedia() != null ? oemApplicant.getMedia().getId() : null)")
    @Mapping(source = "hiredDate", target = "joinDate")
    @Mapping(target = "interviews", ignore = true)
    ApplicantDetailsDto toOemApplicantDetails(OemApplicant oemApplicant);

    @Mapping(target = "prefecture", expression = "java(getPrefecture(operatorApplicant.getPrefecture()))")
    @Mapping(target = "selectionStatusId", expression = "java(operatorApplicant.getSelectionStatus() != null ? operatorApplicant.getSelectionStatus().getId().toString() : null)")
    @Mapping(target = "qualificationIds", expression = "java(getQualificationIds(operatorApplicant.getQualificationIds()))")
    @Mapping(target = "experienceIds", expression = "java(getExperienceIds(operatorApplicant.getExperienceIds()))")
    @Mapping(target = "picId", expression = "java(operatorApplicant.getPic() != null ? operatorApplicant.getPic().getId() : null)")
    @Mapping(target = "projectId", expression = "java(operatorApplicant.getProject() != null ? operatorApplicant.getProject().getId() : null)")
    @Mapping(target = "mediaId", expression = "java(operatorApplicant.getMedia() != null ? operatorApplicant.getMedia().getId() : null)")
    @Mapping(source = "hiredDate", target = "joinDate")
    @Mapping(target = "interviews", ignore = true)
    ApplicantDetailsDto toOperatorApplicantDetails(OperatorApplicant operatorApplicant);

    @Mapping(target = "categoryId", expression = "java(oemApplicantInterview.getCategory() != null ? oemApplicantInterview.getCategory().getId().toString() : null)")
    @Mapping(source = "interviewStartDate", target = "startDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(source = "interviewEndDate", target = "endDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "isDeletable", constant = "true")
    ApplicantInterviewDto toOemApplicantInterviewDetails(OemApplicantInterview oemApplicantInterview);

    @Mapping(target = "categoryId", expression = "java(operatorApplicantInterview.getCategory() != null ? operatorApplicantInterview.getCategory().getId().toString() : null)")
    @Mapping(source = "interviewStartDate", target = "startDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(source = "interviewEndDate", target = "endDateTime", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "isDeletable", constant = "true")
    ApplicantInterviewDto toOperatorApplicantInterviewDetails(OperatorApplicantInterview operatorApplicantInterview);

    @Mapping(target = "prefecture", expression = "java(oemApplicant.getPrefecture() != null ? oemApplicant.getPrefecture().getName() : null)")
    @Mapping(target = "selectionStatus", expression = "java(oemApplicant.getSelectionStatus() != null ? oemApplicant.getSelectionStatus().getItemName() : null)")
    @Mapping(target = "gender", expression = "java(getGenderName(oemApplicant.getGender()))")
    @Mapping(target = "occupation", expression = "java(getOccupationName(oemApplicant.getOccupation()))")
    @Mapping(target = "qualifications", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "joinDate", source = "hiredDate")
    MProfileDto toOemApplicantProfiles(OemApplicant oemApplicant);

    @Mapping(target = "prefecture", expression = "java(operatorApplicant.getPrefecture() != null ? operatorApplicant.getPrefecture().getName() : null)")
    @Mapping(target = "selectionStatus", expression = "java(operatorApplicant.getSelectionStatus() != null ? operatorApplicant.getSelectionStatus().getItemName() : null)")
    @Mapping(target = "gender", expression = "java(getGenderName(operatorApplicant.getGender()))")
    @Mapping(target = "occupation", expression = "java(getOccupationName(operatorApplicant.getOccupation()))")
    @Mapping(target = "qualifications", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "joinDate", source = "hiredDate")
    MProfileDto toOperatorApplicantProfiles(OperatorApplicant operatorApplicant);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "occupation", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "selectionStatus", ignore = true)
    @Mapping(target = "qualificationIds", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "experienceIds", ignore = true)
    @Mapping(target = "interviews", ignore = true)
    @Mapping(source = "joinDate", target = "hiredDate")
    @Mapping(target = "lstStatusChangeDateTime", expression = "java(LocalDateTime.now())")
    OemApplicant toOemApplicant(ApplicantUpsertDto applicantUpsertDto);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "occupation", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "selectionStatus", ignore = true)
    @Mapping(target = "qualificationIds", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "experienceIds", ignore = true)
    @Mapping(target = "interviews", ignore = true)
    @Mapping(source = "joinDate", target = "hiredDate")
    @Mapping(target = "lstStatusChangeDateTime", expression = "java(LocalDateTime.now())")
    OperatorApplicant toOperatorApplicant(ApplicantUpsertDto applicantUpsertDto);

    @Mapping(target = "interviewDate", source = "interviewStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "categoryName", expression = "java(oemApplicantInterview.getCategory().getCategoryName())")
    @Mapping(target = "fullName", expression = "java(oemApplicantInterview.getApplicant().getFullName())")
    @Mapping(target = "mediaName", expression = "java(oemApplicantInterview.getApplicant().getMedia() != null ? oemApplicantInterview.getApplicant().getMedia().getMediaName() : null)")
    @Mapping(target = "projectName", expression = "java(oemApplicantInterview.getApplicant().getProject() != null ? oemApplicantInterview.getApplicant().getProject().getProjectName() : null)")
    DInterviewItemsDto toOemDInterviewItemsDto(OemApplicantInterview oemApplicantInterview);

    @Mapping(target = "interviewDate", source = "interviewStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "categoryName", expression = "java(operatorApplicantInterview.getCategory().getCategoryName())")
    @Mapping(target = "fullName", expression = "java(operatorApplicantInterview.getApplicant().getFullName())")
    @Mapping(target = "mediaName", expression = "java(operatorApplicantInterview.getApplicant().getMedia() != null ? operatorApplicantInterview.getApplicant().getMedia().getMediaName() : null)")
    @Mapping(target = "projectName", expression = "java(operatorApplicantInterview.getApplicant().getProject() != null ? operatorApplicantInterview.getApplicant().getProject().getProjectName() : null)")
    DInterviewItemsDto toOperatorDInterviewItemsDto(OperatorApplicantInterview operatorApplicantInterview);

    @Mapping(source = "startDateTime", target = "interviewStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(source = "endDateTime", target = "interviewEndDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "category", ignore = true)
    OemApplicantInterview toOemApplicantInterview(ApplicantInterviewUpsertDto applicantInterviewUpsertDto);

    @Mapping(source = "startDateTime", target = "interviewStartDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(source = "endDateTime", target = "interviewEndDate", dateFormat = Constants.DATE_TIME_NO_SS_FORMAT)
    @Mapping(target = "category", ignore = true)
    OperatorApplicantInterview toOperatorApplicantInterview(ApplicantInterviewUpsertDto applicantInterviewUpsertDto);

    @Mapping(target = "registeredDate", source = "createdAt")
    @Mapping(target = "mediaName", expression = "java(getMediaName(oemApplicant.getMedia()))")
    @Mapping(target = "projectName", expression = "java(getOemProjectName(oemApplicant.getProject()))")
    @Mapping(target = "age", expression = "java(calculateAge(oemApplicant.getBirthday()))")
    @Mapping(target = "address", expression = "java(getAddress(oemApplicant.getPrefecture(), oemApplicant.getCity()))")
    DApplicantItemsDto toOemDApplicantItemsDto(OemApplicant oemApplicant);

    @Mapping(target = "registeredDate", source = "createdAt")
    @Mapping(target = "mediaName", expression = "java(getMediaName(operatorApplicant.getMedia()))")
    @Mapping(target = "projectName", expression = "java(getOperatorProjectName(operatorApplicant.getProject()))")
    @Mapping(target = "age", expression = "java(calculateAge(operatorApplicant.getBirthday()))")
    @Mapping(target = "address", expression = "java(getAddress(operatorApplicant.getPrefecture(), operatorApplicant.getCity()))")
    DApplicantItemsDto toOperatorDApplicantItemsDto(OperatorApplicant operatorApplicant);

    @Mapping(target = "age", expression = "java(calculateAge(applicant.getBirthday()))")
    DChatItemsDto toDChatItemsDto(ApplicantWithLatestMessagesDto applicant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "occupation", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "selectionStatus", ignore = true)
    @Mapping(target = "qualificationIds", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "experienceIds", ignore = true)
    @Mapping(target = "interviews", ignore = true)
    @Mapping(source = "joinDate", target = "hiredDate")
    void updateToOperatorApplicant(ApplicantUpsertDto form, @MappingTarget OperatorApplicant operatorApplicant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "occupation", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "selectionStatus", ignore = true)
    @Mapping(target = "qualificationIds", ignore = true)
    @Mapping(target = "prefecture", ignore = true)
    @Mapping(target = "experienceIds", ignore = true)
    @Mapping(target = "interviews", ignore = true)
    @Mapping(source = "joinDate", target = "hiredDate")
    void updateToOemApplicant(ApplicantUpsertDto form, @MappingTarget OemApplicant oemApplicant);


    @Mapping(target = "age", ignore = true)
    @Mapping(target = "latestContactDateTime", source = "lstContactDateTime")
    @Mapping(target = "registeredDateTime", source = "createdAt")
    @Mapping(target = "address", expression = "java(getAddress(operatorApplicant.getPrefecture(), operatorApplicant.getCity()))")
    @Mapping(target = "selectionStatusId", expression = "java(getSelectionStatusId(operatorApplicant.getSelectionStatus()))")
    @Mapping(target = "masterMediaName", expression = "java(getMediaName(operatorApplicant.getMedia()))")
    @Mapping(target = "projectName", expression = "java(getOperatorProjectName(operatorApplicant.getProject()))")
    @Mapping(target = "picName", expression = "java(getPicName(operatorApplicant.getPic()))")
    ProjectApplicantItemsDto toProjectApplicantsDto(OperatorApplicant operatorApplicant);

    @Mapping(target = "age", ignore = true)
    @Mapping(target = "latestContactDateTime", source = "lstContactDateTime")
    @Mapping(target = "registeredDateTime", source = "createdAt")
    @Mapping(target = "address", expression = "java(getAddress(oemApplicant.getPrefecture(), oemApplicant.getCity()))")
    @Mapping(target = "selectionStatusId", expression = "java(getSelectionStatusId(oemApplicant.getSelectionStatus()))")
    @Mapping(target = "masterMediaName", expression = "java(getMediaName(oemApplicant.getMedia()))")
    @Mapping(target = "projectName", expression = "java(getOemProjectName(oemApplicant.getProject()))")
    @Mapping(target = "picName", expression = "java(getPicName(oemApplicant.getPic()))")
    ProjectApplicantItemsDto toProjectApplicantsDto(OemApplicant oemApplicant);

    default StatisticsTodayDto getTodayStatistic(final ApplicantDashboardCountDto status) {
        StatisticsTodayDto today = new StatisticsTodayDto();
        today.setTotalApplies((int) status.getTodayApply());
        today.setTotalInterviews((int) status.getTodayInterview());
        today.setTotalOffers((int) status.getTodayOffer());
        today.setTotalAgreements((int) status.getTodayAgreement());
        return today;
    }

    default StatisticsRecentDto getRecentStatistic(final ApplicantDashboardCountDto status) {
        StatisticsTodayDto days = new StatisticsTodayDto();
        days.setTotalApplies((int) status.getLast30DaysApply());
        days.setTotalInterviews((int) status.getLast30DaysInterview());
        days.setTotalOffers((int) status.getLast30DaysOffer());
        days.setTotalAgreements((int) status.getLast30DaysAgreement());

        StatisticsTodayDto lastMonth = new StatisticsTodayDto();
        lastMonth.setTotalApplies((int) status.getLastMonthApply());
        lastMonth.setTotalInterviews((int) status.getLastMonthInterview());
        lastMonth.setTotalOffers((int) status.getLastMonthOffer());
        lastMonth.setTotalAgreements((int) status.getLastMonthAgreement());

        StatisticsTodayDto twoMonths = new StatisticsTodayDto();
        twoMonths.setTotalApplies((int) status.getTwoMonthsApply());
        twoMonths.setTotalInterviews((int) status.getTwoMonthsInterview());
        twoMonths.setTotalOffers((int) status.getTwoMonthsOffer());
        twoMonths.setTotalAgreements((int) status.getTwoMonthsAgreement());

        StatisticsRecentDto recentDto = new StatisticsRecentDto();
        recentDto.setDays(days);
        recentDto.setLastMonth(lastMonth);
        recentDto.setTwoMonths(twoMonths);
        return recentDto;
    }

    @Named("getGenderName")
    default String getGenderName(final String gender) {
        if (Objects.nonNull(gender)) {
            Gender genderEnum = Gender.fromId(gender);
            return genderEnum.getName();
        } else {
            return null;
        }
    }

    @Named("getOccupationName")
    default String getOccupationName(final String occupation) {
        if (Objects.nonNull(occupation)) {
            OccupationType occupationEnum = OccupationType.fromId(occupation);
            return occupationEnum.getName();
        } else {
            return null;
        }
    }

    default String getAddress(final Prefecture prefecture, String city) {
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(prefecture)) {
            builder.append(prefecture.getName());
        }

        if (Objects.nonNull(city)) {
            builder.append(city);
        }
        return builder.toString().isBlank() ? null : builder.toString();
    }

    default String getSelectionStatusId(final SelectionStatus status) {
        return Objects.nonNull(status) ? status.getId().toString() : null;
    }

    default String getMediaName(final MasterMedia media) {
        return Objects.nonNull(media) ? media.getMediaName() : null;
    }

    default String getOperatorProjectName(final OperatorProject project) {
        return Objects.nonNull(project) ? project.getProjectName() : null;
    }

    default String getOemProjectName(final OemProject project) {
        return Objects.nonNull(project) ? project.getProjectName() : null;
    }

    default String getPicName(final OperatorClientAccount account) {
        return Objects.nonNull(account) ? account.getClientName() : null;
    }

    default String getPicName(final OemClientAccount account) {
        return Objects.nonNull(account) ? account.getClientName() : null;
    }

    default List<String> getQualificationIds(String qualificationIds) {
        return qualificationIds != null && !qualificationIds.isEmpty()
                ? Arrays.asList(qualificationIds.split(","))
                : null;
    }

    default List<String> getExperienceIds(String experienceIds) {
        return experienceIds != null && !experienceIds.isEmpty()
                ? Arrays.asList(experienceIds.split(","))
                : null;
    }

    default Integer calculateAge(LocalDate birthday) {
        return (birthday != null) ? Period.between(birthday, LocalDate.now()).getYears() : null;
    }
}
