package jp.co.goalist.gsc.services.criteriaBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jp.co.goalist.gsc.dtos.applicant.ApplicantSearchBoxRequest;
import jp.co.goalist.gsc.dtos.applicant.ApplicantSearchItemsDto;
import jp.co.goalist.gsc.dtos.csv.ApplicantCSVItemsDto;
import jp.co.goalist.gsc.enums.ApplicantStatusType;
import jp.co.goalist.gsc.enums.MessageType;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static jp.co.goalist.gsc.common.Constants.*;

@Service
@RequiredArgsConstructor
public class ApplicantCriteriaBuilder {

    private static final String BASE_SELECT_QUERY = """
            SELECT DISTINCT a.id AS id, a.created_at AS registeredDateTime, a.full_name AS fullName, a.gender AS gender,
            TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE) AS age, CONCAT (pr.name, a.city) AS address, a.selection_status_id AS selectionStatusId,
            mm.media_name AS masterMediaName, oca.client_name AS picName, op.project_name AS projectName,
            a.lst_contact_date_time AS latestContactDateTime, a.memo AS memo,
            a.is_unread AS isUnread,
            case when a.tel is not null then true else false end AS hasTel,
            case when a.email is not null then true else false end AS hasEmail,
            case when a.lst_status_change_date_time IS NULL OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR then true else false end AS isStatusNotChanged,
            (a.is_mail_duplicate OR a.is_tel_duplicate) AS isDuplicate, 1 AS isDeletable, 0 AS isRestricted,
            case when a.blacklist1_id is not null or a.blacklist2_id is not null or a.is_mail_duplicate or a.is_tel_duplicate then false else true end AS isValid,
            (a.blacklist1_id IS NOT NULL OR a.blacklist2_id IS NOT NULL) AS isBlacklist,
            interview_data.interview_date AS interviewDateTime
            """;
    private static final String CSV_SELECT_QUERY = """
            SELECT DISTINCT a.id AS id, a.full_name AS fullName, a.furigana_name AS furiganaName, a.birthday AS birthday, a.gender AS gender,
            op.project_number AS projectId, a.email AS email, a.tel AS tel, a.post_code AS postCode,
            a.prefecture_id AS prefecture, a.city AS city, a.home_address AS homeAddress, a.occupation AS occupation,
            a.selection_status_id AS selectionStatusId, a.qualification_ids AS qualificationIds, a.experience_ids AS experienceIds,
            oca.id AS picId, a.memo AS memo, a.media_id AS mediaId, a.hired_date AS joinedDate, a.is_crawled_data AS isCrawledData,
            op.project_name AS projectName, a.created_at, a.lst_contact_date_time AS latestContactDateTime
            """;
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ApplicantSearchItemsDto> findAllApplicantsByConditions(String parentId, String oemGroupId,
                                                                       ApplicantSearchBoxRequest request) {
        boolean isOperator = !Objects.nonNull(oemGroupId);

        // Build query with param name
        ApplicantQueryBuilder queryBuilder = new ApplicantQueryBuilder(BASE_SELECT_QUERY, isOperator);
        buildWhereClause(queryBuilder, parentId, oemGroupId, request);
        StringBuilder nativeQueryBuilder = queryBuilder.build();
        nativeQueryBuilder.append(sortBy(request.getArrangedBy()));

        // Set parameter to prevent vulnerable
        Query nativeQuery = entityManager.createNativeQuery(nativeQueryBuilder.toString(), ApplicantSearchItemsDto.class);
        queryBuilder.getParameters().forEach(nativeQuery::setParameter);

        // Paging
        Pageable pageable = GeneralUtils.getPagination(request.getPageNumber(), request.getPageSize());
        if (pageable.isPaged()) {
            nativeQuery.setFirstResult((int) pageable.getOffset());
            nativeQuery.setMaxResults(pageable.getPageSize());
        }

        List<ApplicantSearchItemsDto> resultList = (List<ApplicantSearchItemsDto>) nativeQuery.getResultList();
        return new PageImpl<>(resultList, pageable, countQuery(queryBuilder));
    }

    public Stream<ApplicantCSVItemsDto> findAllApplicantsForCSV(String parentId, String oemGroupId,
                                                                ApplicantSearchBoxRequest request) {
        boolean isOperator = !Objects.nonNull(oemGroupId);

        // Build query with param name
        ApplicantQueryBuilder queryBuilder = new ApplicantQueryBuilder(CSV_SELECT_QUERY, isOperator);
        buildWhereClause(queryBuilder, parentId, oemGroupId, request);
        StringBuilder nativeQueryBuilder = queryBuilder.build();
        nativeQueryBuilder.append(sortBy(request.getArrangedBy()));

        // Set parameter to prevent vulnerable
        Query nativeQuery = entityManager.createNativeQuery(nativeQueryBuilder.toString(), ApplicantCSVItemsDto.class);
        queryBuilder.getParameters().forEach(nativeQuery::setParameter);

        // Sort & Paging
        Pageable pageable = GeneralUtils.getPagination(request.getPageNumber(), request.getPageSize());
        if (pageable.isPaged()) {
            nativeQuery.setFirstResult((int) pageable.getOffset());
            nativeQuery.setMaxResults(pageable.getPageSize());
        }

        List<ApplicantCSVItemsDto> resultList = (List<ApplicantCSVItemsDto>) nativeQuery.getResultList();
        return resultList.stream();
    }

    private void buildWhereClause(ApplicantQueryBuilder queryBuilder, String parentId, String oemGroupId,
                                  ApplicantSearchBoxRequest request) {
        queryBuilder.addCondition("a.parent_id = :parentId", Map.of("parentId", parentId));
        queryBuilder.addCondition("a.is_deleted = :isDeleted", Map.of("isDeleted", false));

        if (Objects.nonNull(oemGroupId)) {
            queryBuilder.addCondition("a.oem_group_id = :oemGroupId", Map.of("oemGroupId", oemGroupId));
        }

        addNameCondition(queryBuilder, request.getName(), request.getIsNameExactly());
        addGenderCondition(queryBuilder, request.getGenders());
        addAgeCondition(queryBuilder, request.getMinAge(), request.getMaxAge());
        addSelectionStatusCondition(queryBuilder, request.getSelectionStatusIds());
        addMessageTypeCondition(queryBuilder, request.getMessageType());
        addApplicantStatusCondition(queryBuilder, request.getApplicantStatuses());
        addMediaCondition(queryBuilder, request.getMasterMediaIds());
        addProjectCondition(queryBuilder, request.getProjectName());
        addDateRangeCondition(queryBuilder, request.getApplyStartDate(), request.getApplyEndDate());
        addLocationCondition(queryBuilder, request.getPrefectures(), request.getCity(), request.getAddress());
        addQualificationCondition(queryBuilder, request.getQualificationIds());
        addExperienceCondition(queryBuilder, request.getExperienceIds());
        addStatusChangeCondition(queryBuilder, request.getIsStatusNotChanged());
        addUnreadCondition(queryBuilder, request.getIsUnread());
        addBlacklistCondition(queryBuilder, request.getBlacklistId());
    }

    private void addNameCondition(ApplicantQueryBuilder queryBuilder, String name, Boolean isNameExactly) {
        if (StringUtils.hasText(name)) {
            String condition = isNameExactly
                    ? "a.full_name = :name"
                    : "a.full_name LIKE :name";
            String value = isNameExactly
                    ? name
                    : "%" + GeneralUtils.escapeSpecialCharacter(name) + "%";
            queryBuilder.addCondition(condition, Map.of("name", value));
        }
    }

    private void addGenderCondition(ApplicantQueryBuilder queryBuilder, List<String> genders) {
        if (CollectionUtils.isNotEmpty(genders)) {
            queryBuilder.addInCondition("a.gender", genders);
        }
    }

    private void addAgeCondition(ApplicantQueryBuilder queryBuilder, Integer minAge, Integer maxAge) {
        if (Objects.nonNull(minAge)) {
            queryBuilder.addCondition("TIMESTAMPDIFF(YEAR, a.birthday, CURDATE()) >= :minAge",
                    Map.of("minAge", minAge));
        }
        if (Objects.nonNull(maxAge)) {
            queryBuilder.addCondition("TIMESTAMPDIFF(YEAR, a.birthday, CURDATE()) <= :maxAge",
                    Map.of("maxAge", maxAge));
        }
    }

    private void addSelectionStatusCondition(ApplicantQueryBuilder queryBuilder, List<String> selectionStatusIds) {
        if (CollectionUtils.isNotEmpty(selectionStatusIds)) {
            queryBuilder.addInCondition("a.selection_status_id", selectionStatusIds);
        }
    }

    private void addMessageTypeCondition(ApplicantQueryBuilder queryBuilder, String messageType) {
        if (Objects.nonNull(messageType)) {
            if (Objects.equals(messageType, MessageType.EMAIL.getId())) {
                queryBuilder.addCondition("a.email IS NOT NULL", Collections.emptyMap());
            } else {
                // TODO: not implemented for another type than EMAIL
                queryBuilder.addCondition("1 <> 1", Collections.emptyMap());
            }
        }
    }

    private void addApplicantStatusCondition(ApplicantQueryBuilder queryBuilder,
                                             List<ApplicantStatusType> applicantStatuses) {
        if (CollectionUtils.isNotEmpty(applicantStatuses)) {
            List<String> conditions = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            if (applicantStatuses.contains(ApplicantStatusType.VALID)) {
                conditions.add(
                        "(a.is_mail_duplicate = false AND a.is_tel_duplicate = false AND a.blacklist1_id IS NULL AND a.blacklist2_id IS NULL)");
            }
            if (applicantStatuses.contains(ApplicantStatusType.DUPLICATE)) {
                conditions.add("(a.is_mail_duplicate = true OR a.is_tel_duplicate = true)");
            }
            if (applicantStatuses.contains(ApplicantStatusType.BLACKLIST)) {
                conditions.add("(a.blacklist1_id IS NOT NULL OR a.blacklist2_id IS NOT NULL)");
            }

            if (!conditions.isEmpty()) {
                queryBuilder.addCondition(String.join(SQL_OR_OPERATOR, conditions), params);
            }
        }
    }

    private void addMediaCondition(ApplicantQueryBuilder queryBuilder, List<String> masterMediaIds) {
        if (CollectionUtils.isNotEmpty(masterMediaIds)) {
            queryBuilder.addInCondition("a.media_id", masterMediaIds);
        }
    }

    private void addProjectCondition(ApplicantQueryBuilder queryBuilder, String projectName) {
        if (StringUtils.hasText(projectName)) {
            queryBuilder.addCondition("op.project_name LIKE :projectName",
                    Map.of("projectName", "%" + GeneralUtils.escapeSpecialCharacter(projectName) + "%"));
        }
    }

    private void addDateRangeCondition(ApplicantQueryBuilder queryBuilder, LocalDate applyStartDate,
                                       LocalDate applyEndDate) {
        if (Objects.nonNull(applyStartDate)) {
            queryBuilder.addCondition("date(a.created_at) >= :startDate",
                    Map.of("startDate", applyStartDate));
        }
        if (Objects.nonNull(applyEndDate)) {
            queryBuilder.addCondition("date(a.created_at) <= :endDate",
                    Map.of("endDate", applyEndDate));
        }
    }

    private void addLocationCondition(ApplicantQueryBuilder queryBuilder, List<String> prefectures, String city,
                                      String address) {
        if (CollectionUtils.isNotEmpty(prefectures)) {
            queryBuilder.addInCondition("a.prefecture_id", prefectures);
        }
        if (StringUtils.hasText(city)) {
            queryBuilder.addCondition("a.city LIKE :city",
                    Map.of("city", "%" + GeneralUtils.escapeSpecialCharacter(city) + "%"));
        }
        if (StringUtils.hasText(address)) {
            queryBuilder.addCondition("a.home_address LIKE :address",
                    Map.of("address", "%" + GeneralUtils.escapeSpecialCharacter(address) + "%"));
        }
    }

    private void addQualificationCondition(ApplicantQueryBuilder queryBuilder, List<String> qualificationIds) {
        if (CollectionUtils.isNotEmpty(qualificationIds)) {
            queryBuilder.addFindInSetCondition("a.qualification_ids", qualificationIds);
        }
    }

    private void addExperienceCondition(ApplicantQueryBuilder queryBuilder, List<String> experienceIds) {
        if (CollectionUtils.isNotEmpty(experienceIds)) {
            queryBuilder.addFindInSetCondition("a.experience_ids", experienceIds);
        }
    }

    private void addStatusChangeCondition(ApplicantQueryBuilder queryBuilder, Boolean getIsStatusNotChanged) {
        if (getIsStatusNotChanged) {
            queryBuilder.addCondition(
                    "a.lst_status_change_date_time IS NULL OR a.lst_status_change_date_time < CURRENT_TIMESTAMP - INTERVAL 24 HOUR",
                    Collections.emptyMap());
        }
    }

    private void addUnreadCondition(ApplicantQueryBuilder queryBuilder, Boolean isUnread) {
        if (isUnread) {
            queryBuilder.addCondition("a.is_unread = true", Collections.emptyMap());
        }
    }

    private void addBlacklistCondition(ApplicantQueryBuilder queryBuilder, String blacklistId) {
        if (StringUtils.hasText(blacklistId)) {
            queryBuilder.addCondition(
                    "a.blacklist1_id = :blacklistId OR a.blacklist2_id = :blacklistId",
                    Map.of("blacklistId", blacklistId));
        }
    }

    private long countQuery(ApplicantQueryBuilder queryBuilder) {
        String selectClause = "SELECT COUNT(DISTINCT a.id)";
        String queryClause = String.join(" ", selectClause, queryBuilder.getFromClause(), queryBuilder.getWhereClause());
        Query query = entityManager.createNativeQuery(queryClause);
        queryBuilder.getParameters().forEach(query::setParameter);
        return ((Number) query.getSingleResult()).longValue();
    }

    private String sortBy(String arrangedBy) {
        StringBuilder orderBy = new StringBuilder("ORDER BY");
        String sortQuery = "a.created_at DESC";

        if (StringUtils.hasText(arrangedBy)) {
            String[] arr = arrangedBy.split(":");

            if (arr.length == 2) {
                String field = arr[0];
                if (APPLICANT_ARRANGED_BY.containsKey(field)) {
                    sortQuery = APPLICANT_ARRANGED_BY.get(field) + " " + arr[1];
                }
            }
        }

        orderBy.append(" ");
        orderBy.append(sortQuery);
        return orderBy.toString();
    }

    private static class ApplicantQueryBuilder {

        private final StringBuilder selectClause;

        @Getter
        private final StringBuilder fromClause;

        @Getter
        private final StringBuilder whereClause;

        @Getter
        private final Map<String, Object> parameters;

        private final boolean isOperator;

        public ApplicantQueryBuilder(String selectClause, boolean isOperator) {
            this.selectClause = new StringBuilder(selectClause);
            this.fromClause = new StringBuilder();
            this.whereClause = new StringBuilder();
            this.parameters = new HashMap<>();
            this.isOperator = isOperator;
            buildFromClause();
        }

        private void buildFromClause() {
            String tablePrefix = isOperator ? "operator" : "oem";
            fromClause.append(String.format("""
                    FROM %s_applicants a
                    LEFT JOIN prefectures pr ON a.prefecture_id = pr.id
                    LEFT JOIN %s_projects op ON a.project_id = op.id
                    LEFT JOIN selection_statuses ss ON a.selection_status_id = ss.id
                    LEFT JOIN master_medias mm ON a.media_id = mm.id
                    LEFT JOIN %s_client_accounts oca ON a.pic_id = oca.id
                    LEFT JOIN (
                        SELECT applicant_id, MIN(interview_start_date) AS interview_date
                        FROM %s_applicant_interviews
                        WHERE interview_start_date >= CURDATE()
                        GROUP BY applicant_id
                    ) interview_data ON a.id = interview_data.applicant_id
                    """, tablePrefix, tablePrefix, tablePrefix, tablePrefix));
        }

        public void addCondition(String condition, Map<String, Object> params) {
            String wrapperStatement;
            if (whereClause.isEmpty()) {
                wrapperStatement = GeneralUtils.wrappedStatement(condition, SQL_WHERE_OPERATOR);
            } else {
                wrapperStatement = GeneralUtils.wrappedStatement(condition, SQL_AND_OPERATOR);
            }

            // Set info and params into query builder
            whereClause.append(wrapperStatement);
            parameters.putAll(params);
        }

        public void addInCondition(String field, Collection<String> values) {
            if (values != null && !values.isEmpty()) {
                boolean isNotEnter = values.contains(SEARCH_NOT_ENTER);
                if (isNotEnter) {
                    values = new HashSet<>(values);
                    values.remove(SEARCH_NOT_ENTER);

                    if (values.isEmpty()) {
                        addCondition(String.format("%s IS NULL", field), Collections.emptyMap());
                        return;
                    }
                }

                int paramIndex = 0;
                boolean isNext = false;
                StringBuilder paramNames = new StringBuilder();
                Map<String, Object> paramValues = new HashMap<>();

                for (String v : values) {
                    if (isNext) {
                        paramNames.append(", ");
                    }
                    String paramName = String.format("%s_%d", field, paramIndex);
                    paramNames.append(String.format(":%s", paramName));
                    paramValues.put(String.format("%s", paramName), v);

                    isNext = true;
                    paramIndex++;
                }

                String inStatement = String.format("%s IN (%s)", field, paramNames);
                String notEnterStatement = String.format("%s IS NULL", field);
                if (isNotEnter) {
                    addCondition(String.format("(%s) OR (%s)", notEnterStatement, inStatement), paramValues);
                } else {
                    addCondition(inStatement, paramValues);
                }
            }
        }

        public void addFindInSetCondition(String field, Collection<String> values) {
            if (values != null && !values.isEmpty()) {
                boolean isNotEnter = values.contains(SEARCH_NOT_ENTER);
                if (isNotEnter) {
                    values = new HashSet<>(values);
                    values.remove(SEARCH_NOT_ENTER);

                    if (values.isEmpty()) {
                        addCondition(String.format("%s IS NULL", field), Collections.emptyMap());
                        return;
                    }
                }

                int paramIndex = 0;
                boolean isNext = false;
                StringBuilder paramConditions = new StringBuilder();
                Map<String, Object> paramValues = new HashMap<>();

                for (String v : values) {
                    if (isNext) {
                        paramConditions.append(SQL_OR_OPERATOR);
                    }
                    String paramName = String.format("%s_%d", field, paramIndex);
                    paramConditions.append(String.format("FIND_IN_SET(:%s, %s) > 0", paramName, field));
                    paramValues.put(paramName, v);

                    isNext = true;
                    paramIndex++;
                }

                if (isNotEnter) {
                    addCondition(String.format("(%s IS NULL) OR (%s)", field, paramConditions), paramValues);
                } else {
                    addCondition(paramConditions.toString(), paramValues);
                }
            }
        }

        public StringBuilder build() {
            return new StringBuilder()
                    .append(selectClause).append(" ")
                    .append(fromClause).append(" ")
                    .append(whereClause).append(" ");
        }
    }
}