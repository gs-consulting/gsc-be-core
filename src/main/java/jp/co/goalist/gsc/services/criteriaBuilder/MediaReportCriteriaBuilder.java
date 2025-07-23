package jp.co.goalist.gsc.services.criteriaBuilder;

import jakarta.persistence.*;
import static jp.co.goalist.gsc.common.Constants.*;
import jp.co.goalist.gsc.dtos.mediaReport.*;
import jp.co.goalist.gsc.enums.*;
import jp.co.goalist.gsc.utils.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MediaReportCriteriaBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    private final List<Integer> selectionTypes = List.of(
            FlowType.APPLICATION.getId(),
            FlowType.INTERVIEW.getId(),
            FlowType.OFFER.getId(),
            FlowType.AGREEMENT.getId()
    );
    private final List<Integer> selectionTypesAfter = List.of(
            FlowType.AGREEMENT.getId()
    );

    public List<AdvertReportAfterJoinDto> getMediaCostReportAfterJoin(
            String parentId, String oemGroupId,
            LocalDate startDate, LocalDate endDate,
            DataType dataType, DateType dateType) {

        StringBuilder selectStatement = returnSelectStatement(dataType, dateType, false);

        StringBuilder conditionStatement = createQueryStatement(
                parentId,
                oemGroupId,
                dataType,
                dateType,
                false);

        selectStatement.append(conditionStatement);
        selectStatement.append(" ");
        selectStatement.append(returnGroupBy(dataType, false));

        Query query = entityManager.createNativeQuery(selectStatement.toString(), AdvertReportAfterJoinDto.class);
        query.setParameter("selectionTypes", selectionTypesAfter);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return (List<AdvertReportAfterJoinDto>) query.getResultList();
    }

    public List<AdvertReportBeforeJoinDto> getMediaCostReportBeforeJoin(
            String parentId, String oemGroupId,
            LocalDate startDate, LocalDate endDate,
            DataType dataType, DateType dateType) {

        StringBuilder selectStatement = returnSelectStatement(dataType, dateType, true);

        StringBuilder conditionStatement = createQueryStatement(
                parentId,
                oemGroupId,
                dataType,
                dateType,
                true);

        selectStatement.append(conditionStatement);
        selectStatement.append(" ");
        selectStatement.append(returnGroupBy(dataType, true));

        Query query = entityManager.createNativeQuery(selectStatement.toString(), AdvertReportBeforeJoinDto.class);
        query.setParameter("selectionTypes", selectionTypes);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return (List<AdvertReportBeforeJoinDto>) query.getResultList();
    }

    public List<AdvertTopPersonCount> getColorsBasedOnTopPersonCount(String parentId, String oemGroupId,
                                                                     LocalDate startDate, LocalDate endDate,
                                                                     DataType dataType, DateType dateType,
                                                                     boolean isBeforeJoin) {
        StringBuilder conditionStatement = createQueryStatement(
                parentId,
                oemGroupId,
                dataType,
                dateType,
                isBeforeJoin);

        String queryStatement;

        switch (dataType) {
            case MEDIA_NAME -> {
                queryStatement = "SELECT v.media_id, v.media_name, ROW_NUMBER() OVER (ORDER BY COUNT(distinct v.applicant_id) DESC) AS idx, v.hex_color "
                        + conditionStatement.toString() + " GROUP BY v.media_id, v.media_name";
            }
            case INTERVIEWER -> {
                queryStatement = "SELECT v.interviewer_id, v.interviewer_name, ROW_NUMBER() OVER (ORDER BY COUNT(distinct v.applicant_id) DESC) AS idx, null "
                        + conditionStatement.toString() + " GROUP BY v.interviewer_id, v.interviewer_name";
            }
            case STORE -> {
                queryStatement = "SELECT v.store_id, v.store_name, ROW_NUMBER() OVER (ORDER BY COUNT(distinct v.applicant_id) DESC) AS idx, null "
                        + conditionStatement.toString() + " GROUP BY v.store_id, v.store_name";
            }
            case OCCUPATION -> {
                queryStatement = "SELECT CAST(v.job_type_id AS CHAR(36)), v.job_type_name, ROW_NUMBER() OVER (ORDER BY COUNT(distinct v.applicant_id) DESC) AS idx, null "
                        + conditionStatement.toString() + " GROUP BY v.job_type_id";
            }
            case EDUCATION_OFFICER -> {
                queryStatement = "SELECT v.manager_id, v.manager_name, ROW_NUMBER() OVER (ORDER BY COUNT(distinct v.applicant_id) DESC) AS idx, null "
                        + conditionStatement.toString() + " GROUP BY v.manager_id";
            }
            default -> queryStatement = "";
        }

        if (!queryStatement.isEmpty()) {
            Query query = entityManager.createNativeQuery(queryStatement, AdvertTopPersonCount.class);
            query.setParameter("selectionTypes", selectionTypes);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            return (List<AdvertTopPersonCount>) query.getResultList();
        }

        return new ArrayList<>();
    }

    public StringBuilder returnSelectStatement(
            DataType dataType,
            DateType dateType,
            boolean isBeforeJoin) {

        StringBuilder selectStatement = new StringBuilder();

        switch (dataType) {
            case MEDIA_NAME -> {
                if (isBeforeJoin) {
                    if (Objects.equals(DateType.ADVERTISEMENT_DATE, dateType)) {
                        selectStatement.append(
                                "SELECT v.media_id, v.media_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.ad_start_date) AS each_month, v.advertisement_id, v.applicant_id");
                    } else {
                        selectStatement.append(
                                "SELECT v.media_id, v.media_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.applied_date) AS each_month, v.advertisement_id, v.applicant_id");
                    }
                } else {
                    selectStatement.append("""
                                SELECT v.media_id, v.media_name, v.advertisement_amount, v.advertisement_id, v.applicant_id,
                                COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 7 DAY) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS oneWeekCount,
                                COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 1 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS oneMonthCount,
                                COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 2 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS twoMonthCount,
                                COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 3 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS threeMonthCount,
                                COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 6 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS sixMonthCount""");
                }
            }
            case INTERVIEWER -> {
                if (isBeforeJoin) {
                    if (Objects.equals(DateType.ADVERTISEMENT_DATE, dateType)) {
                        selectStatement.append(
                                "SELECT v.interviewer_id, v.interviewer_name, null, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.ad_start_date) AS each_month, v.advertisement_id, v.applicant_id");
                    } else {
                        selectStatement.append(
                                "SELECT v.interviewer_id, v.interviewer_name, null, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.applied_date) AS each_month, v.advertisement_id, v.applicant_id");
                    }
                }
            }
            case OCCUPATION -> {
                if (isBeforeJoin) {
                    if (Objects.equals(DateType.ADVERTISEMENT_DATE, dateType)) {
                        selectStatement.append(
                                "SELECT CAST(v.job_type_id AS CHAR(36)), v.job_type_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.ad_start_date) AS each_month, v.advertisement_id, v.applicant_id");
                    } else {
                        selectStatement.append(
                                "SELECT CAST(v.job_type_id AS CHAR(36)), v.job_type_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.applied_date) AS each_month, v.advertisement_id, v.applicant_id");
                    }
                }
            }
            case STORE -> {
                if (isBeforeJoin) {
                    if (Objects.equals(DateType.ADVERTISEMENT_DATE, dateType)) {
                        selectStatement.append(
                                "SELECT v.store_id, v.store_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.ad_start_date) AS each_month, v.advertisement_id, v.applicant_id");
                    } else {
                        selectStatement.append(
                                "SELECT v.store_id, v.store_name, v.advertisement_amount, v.selection_status_type, COUNT(distinct v.applicant_id), MONTH(v.applied_date) AS each_month, v.advertisement_id, v.applicant_id");
                    }
                }
            }
            case EDUCATION_OFFICER -> {
                if (!isBeforeJoin) {
                    selectStatement.append("""
                            SELECT v.manager_id, v.manager_name, null, MAX(v.advertisement_id), v.applicant_id,
                            COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 7 DAY) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS oneWeekCount,
                            COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 1 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS oneMonthCount,
                            COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 2 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS twoMonthCount,
                            COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 3 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS threeMonthCount,
                            COUNT(DISTINCT CASE WHEN DATE_ADD(v.hired_date, INTERVAL 6 MONTH) BETWEEN :startDate AND :endDate THEN v.applicant_id END) AS sixMonthCount""");
                }
            }
        }
        return selectStatement;
    }

    public StringBuilder returnGroupBy(
            DataType dataType,
            boolean isBeforeJoin) {

        StringBuilder groupByStatement = new StringBuilder();

        switch (dataType) {
            case MEDIA_NAME -> {
                if (isBeforeJoin) {
                    groupByStatement = new StringBuilder("GROUP BY v.selection_status_type, v.media_id, v.media_name, each_month, v.advertisement_id, v.applicant_id");
                } else {
                    groupByStatement = new StringBuilder("GROUP BY v.media_id, v.media_name, v.advertisement_amount, v.advertisement_id, v.applicant_id");
                }
            }
            case INTERVIEWER -> {
                if (isBeforeJoin) {
                    groupByStatement = new StringBuilder("GROUP BY v.selection_status_type, v.interviewer_id, v.interviewer_name, each_month, v.advertisement_id, v.applicant_id");
                }
            }
            case OCCUPATION -> {
                if (isBeforeJoin) {
                    groupByStatement = new StringBuilder("GROUP BY v.selection_status_type, v.job_type_id, v.job_type_name, each_month, v.advertisement_id, v.applicant_id");
                }
            }
            case STORE -> {
                if (isBeforeJoin) {
                    groupByStatement = new StringBuilder("GROUP BY v.selection_status_type, v.store_id, v.store_name, each_month, v.advertisement_id, v.applicant_id");
                }
            }
            case EDUCATION_OFFICER -> {
                if (!isBeforeJoin) {
                    groupByStatement = new StringBuilder("GROUP BY v.manager_id, v.manager_name, v.advertisement_id, v.applicant_id");
                }
            }
        }
        return groupByStatement;
    }

    StringBuilder createQueryStatement(
            String parentId,
            String oemGroupId,
            DataType dataType,
            DateType dateType,
            boolean isBeforeJoin) {

        boolean isOperator = Objects.isNull(oemGroupId);

        StringBuilder fromStatement = new StringBuilder();

        if (isOperator) {
            fromStatement.append("FROM operator_media_report_count_view v");
        } else {
            fromStatement.append("FROM oem_media_report_count_view v");
        }

        StringBuilder whereStatement = new StringBuilder();
        whereStatement.append(String.format("WHERE v.parent_id = '%s'", parentId));

        if (!isOperator) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    String.format("v.oem_group_id = '%s'", oemGroupId),
                    SQL_AND_OPERATOR));
        }

        if (Objects.equals(dateType, DateType.ADVERTISEMENT_DATE)) {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    "v.ad_start_date <= :endDate",
                    SQL_AND_OPERATOR));

            whereStatement.append(GeneralUtils.wrappedStatement(
                    "v.ad_start_date >= :startDate",
                    SQL_AND_OPERATOR));
        } else {
            whereStatement.append(GeneralUtils.wrappedStatement(
                    "v.applied_date <= :endDate",
                    SQL_AND_OPERATOR));

            whereStatement.append(GeneralUtils.wrappedStatement(
                    "v.applied_date >= :startDate",
                    SQL_AND_OPERATOR));
        }

        whereStatement.append(GeneralUtils.wrappedStatement("v.selection_status_type IN (:selectionTypes)",
                SQL_AND_OPERATOR));

        whereStatement.append(GeneralUtils.wrappedStatement(
                "v.media_id IS NOT NULL",
                SQL_AND_OPERATOR));

        switch (dataType) {
            case MEDIA_NAME -> {
                if (!isBeforeJoin) {
                    whereStatement.append(GeneralUtils.wrappedStatement(
                            "v.hired_date IS NOT NULL AND DATE_ADD(v.hired_date, INTERVAL 7 DAY) BETWEEN :startDate AND :endDate",
                            SQL_AND_OPERATOR));
                }
            }
            case INTERVIEWER -> {
                if (isBeforeJoin) {
                    whereStatement.append(GeneralUtils.wrappedStatement(
                            "v.interviewer_id IS NOT NULL",
                            SQL_AND_OPERATOR));
                }
            }
            case OCCUPATION -> {
                if (isBeforeJoin) {
                    whereStatement.append(GeneralUtils.wrappedStatement(
                            "v.job_type_id IS NOT NULL",
                            SQL_AND_OPERATOR));
                }
            }
            case STORE -> {
                if (isBeforeJoin) {
                    whereStatement.append(GeneralUtils.wrappedStatement(
                            "v.store_id IS NOT NULL",
                            SQL_AND_OPERATOR));
                }
            }
            case EDUCATION_OFFICER -> {
                if (!isBeforeJoin) {
                    whereStatement.append(GeneralUtils.wrappedStatement(
                            "v.is_ini_education_staff = 1 AND v.manager_id IS NOT NULL " +
                                    "AND v.hired_date IS NOT NULL AND DATE_ADD(v.hired_date, INTERVAL 7 DAY) BETWEEN :startDate AND :endDate",
                            SQL_AND_OPERATOR));
                }
            }
        }

        StringBuilder conditionQuery = new StringBuilder();
        conditionQuery.append(" ");
        conditionQuery.append(fromStatement);
        conditionQuery.append(" ");
        conditionQuery.append(whereStatement);
        return conditionQuery;
    }
}