package jp.co.goalist.gsc.services;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.ProjectSearchBoxRequest;
import jp.co.goalist.gsc.dtos.ProjectSearchItemsDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantDashboardCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantTotalCountDto;
import jp.co.goalist.gsc.dtos.applicant.ApplicantWithLatestMessagesDto;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.PublishingStatus;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.gen.dtos.*;
import jp.co.goalist.gsc.repositories.OemApplicantInterviewRepository;
import jp.co.goalist.gsc.repositories.OemApplicantRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantInterviewRepository;
import jp.co.goalist.gsc.repositories.OperatorApplicantRepository;
import jp.co.goalist.gsc.services.criteriaBuilder.ProjectCriteriaBuilder;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.mappers.ApplicantMapper.APPLICANT_MAPPER;
import static jp.co.goalist.gsc.mappers.ProjectMapper.PROJECT_MAPPER;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UtilService utilService;
    private final NotificationService notificationService;
    private final ProjectCriteriaBuilder criteriaBuilder;
    private final OemApplicantInterviewRepository oemApplicantInterviewRepository;
    private final OperatorApplicantInterviewRepository operatorApplicantInterviewRepository;
    private final OemApplicantRepository oemApplicantRepository;
    private final OperatorApplicantRepository operatorApplicantRepository;

    @Transactional
    public StatisticsListDto getApplicationStatistics() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        ApplicantDashboardCountDto statisticData;

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                statisticData = operatorApplicantRepository.countApplicantsForDashboard(parentInfo.getLeft());
            }
            case OEM -> {
                statisticData = oemApplicantRepository.countApplicantsForDashboard(parentInfo.getLeft(), parentInfo.getRight());
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

        StatisticsListDto statisticDataMap = APPLICANT_MAPPER.toStatisticsListDto(statisticData);

        StatisticsRecentDto recentData = new StatisticsRecentDto();
        recentData.setDays(calculateAccumulateData(statisticDataMap.getRecent().getDays()));
        recentData.setLastMonth(calculateAccumulateData(statisticDataMap.getRecent().getLastMonth()));
        recentData.setTwoMonths(calculateAccumulateData(statisticDataMap.getRecent().getTwoMonths()));

        StatisticsListDto finalData = new StatisticsListDto();
        finalData.setToday(statisticDataMap.getToday());
        finalData.setRecent(recentData);

        return finalData;
    }

    public StatisticsTodayDto calculateAccumulateData(StatisticsTodayDto data) {
        int totalApplies = data.getTotalApplies() + data.getTotalInterviews() + data.getTotalOffers() + data.getTotalAgreements();
        int totalInterviews = data.getTotalInterviews() + data.getTotalOffers() + data.getTotalAgreements();
        int totalOffers = data.getTotalOffers() + data.getTotalAgreements();

        StatisticsTodayDto returnData = new StatisticsTodayDto();
        returnData.setTotalApplies(totalApplies);
        returnData.setTotalInterviews(totalInterviews);
        returnData.setTotalOffers(totalOffers);
        returnData.setTotalAgreements(data.getTotalAgreements());

        return returnData;
    }

    @Transactional
    public List<DChatItemsDto> getChats() {
        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<ApplicantWithLatestMessagesDto> applicants = operatorApplicantRepository.findLatestUnreadChatForDashboard(parentInfo.getLeft());
                return applicants.stream().map(APPLICANT_MAPPER::toDChatItemsDto).toList();
            }
            case OEM -> {
                List<ApplicantWithLatestMessagesDto> applicants = oemApplicantRepository.findLatestUnreadChatForDashboard(parentInfo.getLeft(), parentInfo.getRight());
                return applicants.stream().map(APPLICANT_MAPPER::toDChatItemsDto).toList();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public DNotificationDetailsDto getDNotificationDetails(String id) {
        Notification notification = notificationService.getExistingByIdAndStatus(id, PublishingStatus.PUBLIC.getId());
        return PROJECT_MAPPER.toDNotificationDetailsDto(notification);
    }

    @Transactional
    public List<DInterviewItemsDto> getInterviews() {
        Account account = GeneralUtils.getCurrentUser();

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<OperatorApplicantInterview> interviews = operatorApplicantInterviewRepository.getDashboardInterviews(account.getId());
                return interviews.stream().map(APPLICANT_MAPPER::toOperatorDInterviewItemsDto).toList();
            }
            case OEM -> {
                List<OemApplicantInterview> interviews = oemApplicantInterviewRepository.getDashboardInterviews(account.getId());
                return interviews.stream().map(APPLICANT_MAPPER::toOemDInterviewItemsDto).toList();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }
    }

    @Transactional
    public List<DApplicantItemsDto> getNewApplicants() {

        Account account = GeneralUtils.getCurrentUser();
        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);

        switch (SubRole.fromId(account.getSubRole())) {
            case OPERATOR -> {
                List<OperatorApplicant> applicants = operatorApplicantRepository.findLatestApplicantsForDashboard(parentInfo.getLeft());
                return applicants.stream().map(APPLICANT_MAPPER::toOperatorDApplicantItemsDto).toList();
            }
            case OEM -> {
                List<OemApplicant> applicants = oemApplicantRepository.findLatestApplicantsForDashboard(parentInfo.getLeft(), parentInfo.getRight());
                return applicants.stream().map(APPLICANT_MAPPER::toOemDApplicantItemsDto).toList();
            }
            default -> throw new AccessDeniedException(ErrorMessage.PERMISSION_DENIED.getMessage());
        }

    }

    @Transactional
    public List<DNotificationItemsDto> getNotifications() {
        List<Notification> publicNotifications = notificationService.findAllPublicNotifications();
        return publicNotifications.stream().map(PROJECT_MAPPER::toDNotificationItemsDto).toList();
    }

    @Transactional
    public List<DProjectItemsDto> getProjects() {
        Account account = GeneralUtils.getCurrentUser();

        Pair<String, String> parentInfo = utilService.getParentIdAndGroupId(account);
        ProjectSearchBoxRequest request = new ProjectSearchBoxRequest();
        request.setArrangedBy("registeredDate:desc");
        request.setPageNumber(1);
        request.setPageSize(5);
        request.setParentId(parentInfo.getLeft());
        request.setOemGroupId(parentInfo.getRight());

        Page<ProjectSearchItemsDto> projects = criteriaBuilder.findAllProjectsByConditions(request);
        return projects.getContent().stream()
                .map(i -> {
                    List<ApplicantTotalCountDto> applicationStatistics;

                    if (Objects.isNull(parentInfo.getRight())) {
                        applicationStatistics = operatorApplicantRepository.countAllApplicantsForProject(request.getParentId());
                    } else {
                        applicationStatistics = oemApplicantRepository.countAllApplicantsForProject(request.getParentId(), request.getOemGroupId());
                    }

                    Map<String, ApplicantTotalCountDto> applicationStatisticMap = applicationStatistics.stream()
                            .collect(Collectors.toMap(ApplicantTotalCountDto::getCountId, Function.identity()));

                    return PROJECT_MAPPER.toDProjectItemsDto(i, applicationStatisticMap.get(i.getId()));
                })
                .collect(Collectors.toList());
    }

}
