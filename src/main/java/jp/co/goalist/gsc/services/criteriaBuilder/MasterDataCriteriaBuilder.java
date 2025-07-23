package jp.co.goalist.gsc.services.criteriaBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.MasterDataStatusDto;
import jp.co.goalist.gsc.enums.StatusType;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MasterDataCriteriaBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MasterDataStatusDto> getMasterDataStatuses(StatusType statusType, String parentId, String oemGroupId) {
        String queryStatement;
        if (Objects.isNull(oemGroupId)) {
            switch (statusType) {
                case StatusType.PROJECT_STATUS -> {
                    queryStatement = """
                            SELECT distinct m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM operator_projects p
                                  WHERE p.status_id = m.id 
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'PROJECT_STATUS' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.EMPLOYMENT_TYPE -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM operator_projects p
                                  WHERE p.employment_type_id = m.id 
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'EMPLOYMENT_TYPE' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.EXPERIENCE -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                   SELECT 1
                                   FROM operator_applicants a
                                   WHERE FIND_IN_SET(m.id, a.experience_ids) > 0
                                ) THEN 1 ELSE 0
                              END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'EXPERIENCE' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.QUALIFICATION -> {
                    queryStatement = """
                            SELECT distinct m.id, m.status_name as name, m.order,
                            CASE
                            WHEN NOT EXISTS (
                                SELECT 1 FROM operator_applicants a
                                WHERE FIND_IN_SET(m.id, a.qualification_ids) > 0
                            )
                            AND NOT EXISTS (
                                SELECT 1 FROM operator_projects p
                                WHERE p.employment_type_id = m.id 
                            ) THEN 1
                            ELSE 0
                            END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'QUALIFICATION' AND m.parent_id = :parentId
                            ORDER BY m.order ASC;
                            """;
                }
                case StatusType.OCCUPATION -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM operator_projects p
                                  WHERE p.occupation_id = m.id
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'OCCUPATION' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.INTERVIEW_LOC -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM operator_projects p
                                  WHERE p.interview_venue_id = m.id
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'INTERVIEW_LOC' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.WORKING_PERIOD -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM operator_projects p
                                  WHERE p.working_period_id = m.id
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'WORKING_PERIOD' AND m.parent_id = :parentId
                            ORDER BY m.order ASC
                            """;
                }
                default -> throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(ErrorMessage.INVALID_DATA.getMessage())
                        .fieldError("種類")
                        .build());
            }
        } else {
            switch (statusType) {
                case StatusType.PROJECT_STATUS -> {
                    queryStatement = """
                            SELECT distinct m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_projects p
                                  WHERE p.status_id = m.id 
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'PROJECT_STATUS' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.EMPLOYMENT_TYPE -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_projects p
                                  WHERE p.employment_type_id = m.id 
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'EMPLOYMENT_TYPE' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.EXPERIENCE -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_applicants a
                                  WHERE FIND_IN_SET(m.id, a.experience_ids) > 0
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'EXPERIENCE' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.QUALIFICATION -> {
                    queryStatement = """
                            SELECT distinct m.id, m.status_name as name, m.order,
                            CASE
                            WHEN NOT EXISTS (
                                SELECT 1 FROM oem_applicants a
                                WHERE FIND_IN_SET(m.id, a.qualification_ids) > 0
                            )
                            AND NOT EXISTS (
                                SELECT 1 FROM oem_projects p
                                WHERE p.employment_type_id = m.id 
                            ) THEN 1
                            ELSE 0
                            END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'QUALIFICATION' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC;
                            """;
                }
                case StatusType.OCCUPATION -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_projects p
                                  WHERE p.occupation_id = m.id 
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'OCCUPATION' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.INTERVIEW_LOC -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_projects p
                                  WHERE p.interview_venue_id = m.id
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'INTERVIEW_LOC' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                case StatusType.WORKING_PERIOD -> {
                    queryStatement = """
                            SELECT m.id, m.status_name as name, m.order,
                            CASE WHEN NOT EXISTS (
                                  SELECT 1
                                  FROM oem_projects p
                                  WHERE p.working_period_id = m.id
                               ) THEN 1 ELSE 0
                             END AS isDeletable
                            FROM master_statuses m
                            WHERE m.type = 'WORKING_PERIOD' AND m.parent_id = :parentId and m.oem_group_id = :oemGroupId
                            ORDER BY m.order ASC
                            """;
                }
                default -> throw new BadValidationException(ErrorResponse.builder()
                        .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                        .message(ErrorMessage.INVALID_DATA.getMessage())
                        .fieldError("種類")
                        .build());
            }
        }

        Query query = entityManager.createNativeQuery(queryStatement)
                .setParameter("parentId", parentId);

        if (Objects.nonNull(oemGroupId)) {
            query.setParameter("oemGroupId", oemGroupId);
        }

        List<Object[]> result = query.getResultList();

        return result.stream().map(row -> new MasterDataStatusDto(
                (Long) row[0],
                (String) row[1],
                (Integer) row[2],
                toBoolean(row[3])
        )).toList();
    }

    private Boolean toBoolean(Object value) {
        if (value instanceof Boolean boolVal) {
            return boolVal;
        } else if (value instanceof Number numVal) {
            return numVal.intValue() != 0;
        } else if (value != null) {
            return Boolean.parseBoolean(value.toString());
        } else {
            return false;
        }
    }
}
