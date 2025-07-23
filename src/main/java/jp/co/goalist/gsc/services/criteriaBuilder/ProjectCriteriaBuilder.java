package jp.co.goalist.gsc.services.criteriaBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.ProjectPermissionUpsertMappingDto;
import jp.co.goalist.gsc.dtos.ProjectPermissionsDto;
import jp.co.goalist.gsc.dtos.ProjectSearchBoxRequest;
import jp.co.goalist.gsc.dtos.ProjectSearchItemsDto;
import jp.co.goalist.gsc.dtos.csv.ProjectCSVItemsDto;
import jp.co.goalist.gsc.enums.AbAdjustment;
import jp.co.goalist.gsc.enums.ExperienceStatus;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.gen.dtos.ProjectSearchBoxDto;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static jp.co.goalist.gsc.common.Constants.*;

@Service
@RequiredArgsConstructor
public class ProjectCriteriaBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProjectSearchItemsDto> findAllProjectsByConditions(ProjectSearchBoxRequest projectSearchBoxRequest) {
        StringBuilder selectStatement = new StringBuilder("""
                SELECT p.id as id, p.project_number as projectNo, p.project_name as projectName,
                st.status_name as statusName, p.status_id as statusId, occ.status_name as occupation,
                null as totalApplies, null as totalInterviews, null as totalOffers, null as totalAgreements,
                p.goal_apply as goalApply, p.goal_interview as goalInterview, p.goal_offer as goalOffer, p.goal_agreement as goalAgreement,
                GROUP_CONCAT(q.status_name) as qualifications,
                CONCAT(IFNULL(pr.name, ''), " ", IFNULL(c.name, '')) as workplace,
                p.ab_adjustment as abAdjustment, p.memo as memo, p.created_at as registeredDate,
                case when p.is_summarized = true and p.grouped_by = 'OCCUPATION' and p.occupation_id is not null then 1 else 0 end as isGroupedByOccupation,
                case when p.is_summarized = true and p.grouped_by = 'BRANCH' and p.branch_id is not null then 1 else 0 end as isGroupedByBranch,
                case when (p.branch_id is not null) then 1 else 0 end as hasBranchAuthority,
                p.permission
                """);

        if (Objects.nonNull(projectSearchBoxRequest)) {
//            if (Objects.equals(projectSearchBoxRequest.getEmploymentType(), EmploymentType.STAFF.getId())) {
//                selectStatement.append(", case when b.staff_permission = 'EDIT' then 1 else 0 end as hasEmploymentTypePermission");
//            } else if (Objects.equals(projectSearchBoxRequest.getEmploymentType(), EmploymentType.PART.getId())) {
//                selectStatement.append(", case when b.part_time_permission = 'EDIT' then 1 else 0 end as hasEmploymentTypePermission");
//            } else {
            selectStatement.append(", null as hasEmploymentTypePermission");
//            }
        }

        StringBuilder fromStatement = returnFromStatement(
                Objects.isNull(projectSearchBoxRequest.getOemGroupId()),
                Objects.nonNull(projectSearchBoxRequest.getBranchIds())
        );

        StringBuilder conditionStatement = createQueryStatement(
                fromStatement,
                projectSearchBoxRequest.getParentId(),
                projectSearchBoxRequest.getOemGroupId(),
                projectSearchBoxRequest.getProjectNo(),
                projectSearchBoxRequest.getProjectName(),
                projectSearchBoxRequest.getStatusIds(),
                projectSearchBoxRequest.getRegisteredStartDate(),
                projectSearchBoxRequest.getRegisteredEndDate(),
                projectSearchBoxRequest.getOccupationIds(),
                projectSearchBoxRequest.getIsShiftSystem(),
                projectSearchBoxRequest.getExperience(),
                projectSearchBoxRequest.getQualificationIds(),
                projectSearchBoxRequest.getQualificationNotes(),
                projectSearchBoxRequest.getPrefecture(),
                projectSearchBoxRequest.getCity(),
                projectSearchBoxRequest.getWard(),
                projectSearchBoxRequest.getHoliday(),
                projectSearchBoxRequest.getBenefit(),
                projectSearchBoxRequest.getBranchIds(),
                projectSearchBoxRequest.getStoreName(),
                projectSearchBoxRequest.getWorkingPeriodId(),
                projectSearchBoxRequest.getAbAdjustment()
        );
        selectStatement.append(conditionStatement);
        long totalItems = countQuery(conditionStatement, projectSearchBoxRequest);

        String groupBy = "GROUP BY p.id";
        selectStatement.append(" ");
        selectStatement.append(groupBy);
        selectStatement.append(" ");
        selectStatement.append(sortBy(projectSearchBoxRequest.getArrangedBy()));

        Query query = entityManager.createNativeQuery(selectStatement.toString(), ProjectSearchItemsDto.class);
        query = addTextParametersAndPagination(query,
                projectSearchBoxRequest.getProjectNo(),
                projectSearchBoxRequest.getProjectName(),
                projectSearchBoxRequest.getQualificationNotes(),
                projectSearchBoxRequest.getWard(),
                projectSearchBoxRequest.getHoliday(),
                projectSearchBoxRequest.getBenefit(),
                projectSearchBoxRequest.getStoreName(),
                null);

        // Apply pagination
        Pageable pageable = GeneralUtils.getPagination(projectSearchBoxRequest.getPageNumber(), projectSearchBoxRequest.getPageSize());
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        List<ProjectSearchItemsDto> resultList = (List<ProjectSearchItemsDto>) query.getResultList();
        return new PageImpl<>(resultList, pageable, totalItems);
    }

    public List<ProjectPermissionsDto> findAllProjectsForPermission(String parentId, String oemGroupId, String branchId, ProjectSearchBoxDto projectSearchBoxDto) {
        StringBuilder selectStatement = new StringBuilder("""
                SELECT p.id as id, p.project_number as projectNo, p.project_name as projectName, coalesce(p.permission, 'EDIT') as permission
                """);

        StringBuilder fromStatement = returnFromStatement(
                Objects.isNull(oemGroupId),
                Objects.nonNull(projectSearchBoxDto.getBranchIds()));

        StringBuilder conditionStatement = createQueryStatement(
                fromStatement,
                parentId,
                oemGroupId,
                projectSearchBoxDto.getProjectNo(),
                projectSearchBoxDto.getProjectName(),
                projectSearchBoxDto.getStatusIds(),
                projectSearchBoxDto.getRegisteredStartDate(),
                projectSearchBoxDto.getRegisteredEndDate(),
                projectSearchBoxDto.getOccupationIds(),
                projectSearchBoxDto.getIsShiftSystem(),
                projectSearchBoxDto.getExperience(),
                projectSearchBoxDto.getQualificationIds(),
                projectSearchBoxDto.getQualificationNotes(),
                projectSearchBoxDto.getPrefecture(),
                projectSearchBoxDto.getCity(),
                projectSearchBoxDto.getWard(),
                projectSearchBoxDto.getHoliday(),
                projectSearchBoxDto.getBenefit(),
                projectSearchBoxDto.getBranchIds(),
                projectSearchBoxDto.getStoreName(),
                projectSearchBoxDto.getWorkingPeriodId(),
                projectSearchBoxDto.getAbAdjustment()
        );

        conditionStatement.append(GeneralUtils.wrappedStatement(
                "(case when (:branchId is null) then p.branch_id is null else p.branch_id = :branchId end)",
                SQL_AND_OPERATOR)
        );

        selectStatement.append(conditionStatement);

        String groupBy = "GROUP BY p.id";
        selectStatement.append(" ");
        selectStatement.append(groupBy);
        selectStatement.append(" ");
        selectStatement.append(sortBy(projectSearchBoxDto.getArrangedBy()));

        Query query = entityManager.createNativeQuery(selectStatement.toString(), ProjectPermissionsDto.class);
        query = addTextParametersAndPagination(query,
                projectSearchBoxDto.getProjectNo(),
                projectSearchBoxDto.getProjectName(),
                projectSearchBoxDto.getQualificationNotes(),
                projectSearchBoxDto.getWard(),
                projectSearchBoxDto.getHoliday(),
                projectSearchBoxDto.getBenefit(),
                projectSearchBoxDto.getStoreName(),
                branchId);

        return (List<ProjectPermissionsDto>) query.getResultList();
    }

    public Stream<ProjectCSVItemsDto> findAllProjectsForCSV(String parentId, String oemGroupId, ProjectSearchBoxRequest projectSearchBoxRequest) {
        StringBuilder selectStatement = new StringBuilder("""
                SELECT p.project_number as projectNo, p.project_name as projectName, p.status_id, p.branch_id,
                p.store_id as storeId, p.deadline, p.working_hours1, p.working_hours2, p.working_hours3, p.working_days,
                p.occupation_id, occ.status_name as occupation, p.salary_type, p.salary_amount,
                p.salary_notes, p.employment_type_id, p.recruiting_number, p.gender_restriction, p.min_age, p.max_age,
                p.not_hiring_condition, p.working_period_id, p.desired_start_date, p.job_description, p.remarks,
                p.interview_venue_id as interviewVenueId, p.employment_period_start, p.employment_period_end, p.is_shift_system,
                p.experience_status, p.portraits, p.qualifications, p.qualification_notes as qualificationNotes, p.prefecture_id,
                p.city_id, p.ward, p.working_hours_notes, p.holidays, p.benefits, p.ab_adjustment AS abAdjustment, p.memo,
                p.goal_apply, p.goal_interview, p.goal_offer, p.goal_agreement,
                CONCAT(IFNULL(pr.name, ''), " ", IFNULL(c.name, '')) as workplace, p.created_at AS registeredDate
                """);

        StringBuilder fromStatement = returnFromStatement(
                Objects.isNull(oemGroupId),
                Objects.nonNull(projectSearchBoxRequest.getBranchIds())
        );

        StringBuilder conditionStatement = createQueryStatement(
                fromStatement,
                parentId,
                oemGroupId,
                projectSearchBoxRequest.getProjectNo(),
                projectSearchBoxRequest.getProjectName(),
                projectSearchBoxRequest.getStatusIds(),
                projectSearchBoxRequest.getRegisteredStartDate(),
                projectSearchBoxRequest.getRegisteredEndDate(),
                projectSearchBoxRequest.getOccupationIds(),
                projectSearchBoxRequest.getIsShiftSystem(),
                projectSearchBoxRequest.getExperience(),
                projectSearchBoxRequest.getQualificationIds(),
                projectSearchBoxRequest.getQualificationNotes(),
                projectSearchBoxRequest.getPrefecture(),
                projectSearchBoxRequest.getCity(),
                projectSearchBoxRequest.getWard(),
                projectSearchBoxRequest.getHoliday(),
                projectSearchBoxRequest.getBenefit(),
                projectSearchBoxRequest.getBranchIds(),
                projectSearchBoxRequest.getStoreName(),
                projectSearchBoxRequest.getWorkingPeriodId(),
                projectSearchBoxRequest.getAbAdjustment()
        );
        selectStatement.append(conditionStatement);

        String groupBy = "GROUP BY p.id";
        selectStatement.append(" ");
        selectStatement.append(groupBy);
        selectStatement.append(" ");
        selectStatement.append(sortBy(projectSearchBoxRequest.getArrangedBy()));

        Query query = entityManager.createNativeQuery(selectStatement.toString(), ProjectCSVItemsDto.class);
        query = addTextParametersAndPagination(query,
                projectSearchBoxRequest.getProjectNo(),
                projectSearchBoxRequest.getProjectName(),
                projectSearchBoxRequest.getQualificationNotes(),
                projectSearchBoxRequest.getWard(),
                projectSearchBoxRequest.getHoliday(),
                projectSearchBoxRequest.getBenefit(),
                projectSearchBoxRequest.getStoreName(),
                null);

        List<ProjectCSVItemsDto> resultList = (List<ProjectCSVItemsDto>) query.getResultList();
        return resultList.stream();
    }

    public Query addTextParametersAndPagination(Query query,
                                                String projectNo,
                                                String projectName,
                                                String qualificationNotes,
                                                String ward,
                                                String holiday,
                                                String benefit,
                                                String storeName,
                                                String branchId) {
        if (Objects.nonNull(branchId) && !branchId.isBlank()) {
            query.setParameter("branchId", branchId);
        }
        if (Objects.nonNull(projectNo) && !projectNo.isBlank()) {
            query.setParameter("projectNo", GeneralUtils.wrapToLikeWithSpecialCharacter(projectNo));
        }
        if (Objects.nonNull(projectName) && !projectName.isBlank()) {
            query.setParameter("projectName", GeneralUtils.wrapToLikeWithSpecialCharacter(projectName));
        }
        if (Objects.nonNull(qualificationNotes) && !qualificationNotes.isBlank()) {
            query.setParameter("qualificationNotes", GeneralUtils.wrapToLikeWithSpecialCharacter(qualificationNotes));
        }
        if (Objects.nonNull(ward) && !ward.isBlank()) {
            query.setParameter("ward", GeneralUtils.wrapToLikeWithSpecialCharacter(ward));
        }
        if (Objects.nonNull(holiday) && !holiday.isBlank()) {
            query.setParameter("holiday", GeneralUtils.wrapToLikeWithSpecialCharacter(holiday));
        }
        if (Objects.nonNull(benefit) && !benefit.isBlank()) {
            query.setParameter("benefit", GeneralUtils.wrapToLikeWithSpecialCharacter(benefit));
        }
        if (Objects.nonNull(storeName) && !storeName.isBlank()) {
            query.setParameter("storeName", GeneralUtils.wrapToLikeWithSpecialCharacter(storeName));
        }

        return query;
    }

    private long countQuery(StringBuilder conditionStatement, ProjectSearchBoxRequest projectSearchBoxRequest) {
        String countSelectQuery = "SELECT COUNT(DISTINCT p.id) ";
        String countStatement = countSelectQuery + conditionStatement;
        Query countQuery = entityManager.createNativeQuery(countStatement, Long.class);
        countQuery = addTextParametersAndPagination(countQuery,
                projectSearchBoxRequest.getProjectNo(),
                projectSearchBoxRequest.getProjectName(),
                projectSearchBoxRequest.getQualificationNotes(),
                projectSearchBoxRequest.getWard(),
                projectSearchBoxRequest.getHoliday(),
                projectSearchBoxRequest.getBenefit(),
                projectSearchBoxRequest.getStoreName(),
                null);

        return Objects.nonNull(countQuery.getSingleResult()) ? (long) countQuery.getSingleResult() : 0;
    }

    StringBuilder returnFromStatement(boolean isOperator, boolean checkBranch) {
        StringBuilder fromStatement;
        if (isOperator) {
            fromStatement = new StringBuilder("FROM operator_projects p");
        } else {
            fromStatement = new StringBuilder("FROM oem_projects p");
        }

        fromStatement.append(" ");
        fromStatement.append("""
                left join prefectures pr on p.prefecture_id = pr.id
                left join cities c on p.city_id = c.id
                left join master_statuses st on st.id = p.status_id and st.type = 'PROJECT_STATUS'
                left join master_statuses occ on occ.id = p.occupation_id and occ.type = 'OCCUPATION'
                left join master_statuses q ON FIND_IN_SET(q.id, p.qualifications) > 0""");

        return fromStatement;
    }

    StringBuilder createQueryStatement(StringBuilder fromStatement,
                                       String parentId,
                                       String oemGroupId,
                                       String projectNo,
                                       String projectName,
                                       List<String> statusIds,
                                       LocalDate registeredStartDate,
                                       LocalDate registeredEndDate,
                                       List<String> occupationIds,
                                       String isShiftSystem,
                                       String experience,
                                       List<String> qualificationIds,
                                       String qualificationNotes,
                                       String prefectureId,
                                       String cityId,
                                       String ward,
                                       String holiday,
                                       String benefit,
                                       List<String> branchNames,
                                       String storeName,
                                       String workingPeriodId,
                                       String abAdjustment) {
        boolean isOperator = !Objects.nonNull(oemGroupId);
        StringBuilder conditionQuery = new StringBuilder();
        StringBuilder whereStatement = new StringBuilder();
        whereStatement.append(String.format("WHERE p.parent_id = '%s'", parentId));

        if (!isOperator) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    String.format("p.oem_group_id = '%s'", oemGroupId),
                    SQL_AND_OPERATOR)
            );
        }

        if (Objects.nonNull(projectNo) && !projectNo.isBlank()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.likeStatementWithoutWildcard("p.project_number", ":projectNo", ""),
                    SQL_AND_OPERATOR)
            );
        }

        if (Objects.nonNull(projectName) && !projectName.isBlank()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.likeStatementWithoutWildcard("p.project_name", ":projectName", ""),
                    SQL_AND_OPERATOR)
            );
        }

        // ステータス
        if (Objects.nonNull(statusIds) && !statusIds.isEmpty()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.createInStatement("p.status_id", new HashSet<>(statusIds)),
                    SQL_AND_OPERATOR)
            );
        }

        // 登録日
        if (Objects.nonNull(registeredStartDate)) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    String.format("date(p.created_at) >= '%s'", registeredStartDate.format(dateFormatter)),
                    SQL_AND_OPERATOR)
            );
        }

        if (Objects.nonNull(registeredEndDate)) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    String.format("date(p.created_at) <= '%s'", registeredEndDate.format(dateFormatter)),
                    SQL_AND_OPERATOR)
            );
        }

        // 職種
        if (Objects.nonNull(occupationIds) && !occupationIds.isEmpty()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.createInStatement("p.occupation_id", new HashSet<>(occupationIds)),
                    SQL_AND_OPERATOR)
            );
        }

        // シフト制
        if (Objects.nonNull(isShiftSystem) && !isShiftSystem.isBlank()) {
            if (!SHIFT_SYSTEM.contains(isShiftSystem)) {
                throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                "シフト制"))
                        .build());
            }

            if (!Objects.equals(SHIFT_SYSTEM.get(2), isShiftSystem)) {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        GeneralUtils.equalStatement("p.is_shift_system", isShiftSystem),
                        SQL_AND_OPERATOR
                ));
            } else {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        "p.is_shift_system IS NULL",
                        SQL_AND_OPERATOR
                ));
            }
        }

        // 経験の有無
        if (Objects.nonNull(experience) && !experience.isBlank()) {
            ExperienceStatus experienceStatus = ExperienceStatus.fromId(experience);
            if (!Objects.equals(experienceStatus, ExperienceStatus.NONE)) {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        GeneralUtils.equalStatement("p.experience_status", experienceStatus.getId()),
                        SQL_AND_OPERATOR
                ));
            } else {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        "p.experience_status IS NULL",
                        SQL_AND_OPERATOR
                ));
            }
        }

        // 資格
        if (Objects.nonNull(qualificationIds) && !qualificationIds.isEmpty()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.createFindInSetStatement("p.qualifications", new HashSet<>(qualificationIds), SQL_OR_OPERATOR),
                    SQL_AND_OPERATOR)
            );
        }

        // 資格補足
        if (Objects.nonNull(qualificationNotes) && !qualificationNotes.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.likeStatementWithoutWildcard("p.qualification_notes", ":qualificationNotes", ""),
                            SQL_AND_OPERATOR)
            );
        }

        // 勤務地（都道府県)
        if (Objects.nonNull(prefectureId) && !prefectureId.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.equalStatement("p.prefecture_id", prefectureId),
                            SQL_AND_OPERATOR)
            );
        }

        // 勤務地（市区群）
        if (Objects.nonNull(cityId) && !cityId.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.equalStatement("p.city_id", cityId),
                            SQL_AND_OPERATOR)
            );
        }

        // 勤務地（市区群以下）
        if (Objects.nonNull(ward) && !ward.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.likeStatementWithoutWildcard("p.ward", ":ward", ""),
                            SQL_AND_OPERATOR)
            );
        }

        // 休日
        if (Objects.nonNull(holiday) && !holiday.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.likeStatementWithoutWildcard("p.holidays", ":holiday", ""),
                            SQL_AND_OPERATOR)
            );
        }

        // 福利厚生
        if (Objects.nonNull(benefit) && !benefit.isBlank()) {
            whereStatement.append(
                    GeneralUtils.wrappedStatement(
                            GeneralUtils.likeStatementWithoutWildcard("p.benefits", ":benefit", ""),
                            SQL_AND_OPERATOR)
            );
        }

        // 支店
        if (Objects.nonNull(branchNames) && !branchNames.isEmpty()) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.createFindInSetStatement("p.branch_id", new HashSet<>(branchNames), SQL_OR_OPERATOR),
                    SQL_AND_OPERATOR)
            );
        }

        // 拠点・店舗
        if (Objects.nonNull(storeName) && !storeName.isBlank()) {
            if (isOperator) {
                fromStatement.append(" ").append("join operator_stores s on p.store_id = s.id ");
            } else {
                fromStatement.append(" ").append("join oem_stores s on p.store_id = s.id ");
            }
            whereStatement.append(GeneralUtils.wrappedStatement(
                    GeneralUtils.likeStatementWithoutWildcard("s.store_name", ":storeName", ""),
                    SQL_AND_OPERATOR)
            );
        }

        // 長期休暇
        if (Objects.nonNull(workingPeriodId) && !workingPeriodId.isBlank()) {
            if (!Objects.equals(workingPeriodId, SEARCH_NOT_ENTER)) {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        GeneralUtils.equalStatement("p.working_period_id", workingPeriodId),
                        SQL_AND_OPERATOR
                ));
            } else {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        "p.working_period_id IS NULL",
                        SQL_AND_OPERATOR
                ));
            }
        }

        // AB判定
        if (Objects.nonNull(abAdjustment) && !abAdjustment.isBlank()) {
            AbAdjustment ab = AbAdjustment.fromId(abAdjustment);
            if (!Objects.equals(ab, AbAdjustment.NONE)) {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        GeneralUtils.equalStatement("p.ab_adjustment", ab.getId()),
                        SQL_AND_OPERATOR
                ));
            } else {
                whereStatement.append(GeneralUtils.wrappedStatement(
                        "p.ab_adjustment IS NULL",
                        SQL_AND_OPERATOR
                ));
            }
        }

        conditionQuery.append(" ");
        conditionQuery.append(fromStatement);
        conditionQuery.append(" ");
        conditionQuery.append(whereStatement);
        return conditionQuery;
    }

    private String sortBy(String arrangedBy) {
        StringBuilder orderBy = new StringBuilder("ORDER BY");
        String defaultField = "p.created_at DESC";

        if (Objects.nonNull(arrangedBy) && !arrangedBy.isBlank()) {
            String[] arr = arrangedBy.split(":");

            if (arr.length == 2) {
                String field = arr[0];
                if (PROJECT_ARRANGED_BY.containsKey(field)) {
                    defaultField = PROJECT_ARRANGED_BY.get(field) + " " + arr[1];
                }
            }
        }

        orderBy.append(" ");
        orderBy.append(defaultField);
        return orderBy.toString();
    }

    public void updateForProjectPermissions(List<ProjectPermissionUpsertMappingDto> updates, boolean isOperator) {
        StringBuilder queryBuilder;
        if (isOperator) {
            queryBuilder = new StringBuilder("UPDATE operator_projects p SET p.permission = CASE ");
        } else {
            queryBuilder = new StringBuilder("UPDATE oem_projects p SET p.permission = CASE ");
        }

        for (ProjectPermissionUpsertMappingDto item : updates) {
            queryBuilder.append("WHEN p.id = '").append(item.getId()).append("' THEN '").append(item.getPermission()).append("' ");
        }

        queryBuilder.append("ELSE p.permission END");

        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        query.executeUpdate();
    }
}
