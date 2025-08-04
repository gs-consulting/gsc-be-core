package jp.co.goalist.gsc.services.criteriaBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jp.co.goalist.gsc.dtos.OemClientAccountItemsDto;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OemClientAccountCriteriaBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<OemClientAccountItemsDto> findAllProjectsByConditions(String oemAccountId,
                                                                      String oemGroupId,
                                                                      String searchInput,
                                                                      Integer pageNumber,
                                                                      Integer pageSize) {
        StringBuilder selectStatement = new StringBuilder("""
                SELECT
                oca.id,
                oca.oem_group_id AS oemGroupId,
                oca.client_name AS clientName,
                manager_tbl.managerNames AS managerNames,
                GROUP_CONCAT(DISTINCT b.id) AS branchIds,
                GROUP_CONCAT(DISTINCT b.branch_name) AS branchNames,
                GROUP_CONCAT(DISTINCT s.store_name) AS storeNames,
                a.enabled AS isMember,
                a.reset_token_string AS resetTokenString,
                a.token_expiration_date AS tokenExpirationDate,
                oca.created_at AS createdAt
                """);

        String fromStatementForOperator = returnFromStatement(true);
        String fromStatementForOem = returnFromStatement(false);
        String conditionStatementForOperator = """
                        WHERE oca.oem_group_id = :oemGroupId AND oca.parent_id IS NULL
                        AND (:searchInput IS NULL OR oca.client_name like :searchInput)
                        AND oca.is_deleted = false
                        """;
        String conditionStatementForOem = """
                        WHERE oca.oem_group_id = :oemGroupId AND oca.oem_account_id = :oemAccountId
                        AND oca.parent_id IS NULL
                        AND (:searchInput IS NULL OR oca.client_name like :searchInput)
                        AND oca.is_deleted = false
                        """;

        String groupByStatement = "GROUP BY oca.id";

        String statement1 = selectStatement + fromStatementForOperator +
                conditionStatementForOperator + groupByStatement;
        String statement2 = selectStatement + fromStatementForOem +
                conditionStatementForOem + groupByStatement;

        String unionStatement = GeneralUtils.createUnionStatement(statement1, statement2);
        String selectQuery = String.format("SELECT * FROM (%s) AS temp ORDER BY temp.createdAt ASC", unionStatement);

        Query query = entityManager.createNativeQuery(selectQuery, OemClientAccountItemsDto.class);
        query.setParameter("oemGroupId", oemGroupId);
        query.setParameter("oemAccountId", oemAccountId);
        query.setParameter("searchInput", GeneralUtils.wrapToLike(searchInput));

        // Apply pagination
        Pageable pageable = GeneralUtils.getPagination(pageNumber, pageSize);
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        List<OemClientAccountItemsDto> resultList = (List<OemClientAccountItemsDto>) query.getResultList();
        return new PageImpl<>(resultList, pageable, countQuery(unionStatement, oemGroupId, oemAccountId, searchInput));
    }

    String returnFromStatement(boolean isOperator) {
        String fromStatement;
        if (isOperator) {
            fromStatement = """
                    FROM
                        operator_client_accounts oca
                            JOIN
                        accounts a ON a.id = oca.id
                            LEFT JOIN
                        (SELECT
                            m.operator_client_id AS managerId,
                                GROUP_CONCAT(a.client_name) AS managerNames
                        FROM
                            operator_client_managers m
                        JOIN operator_client_accounts a ON a.id = m.operator_client_manager_id
                        GROUP BY m.operator_client_id) AS manager_tbl ON manager_tbl.managerId = oca.id
                            LEFT JOIN
                        operator_client_locations loc ON loc.operator_client_id = oca.id
                            LEFT JOIN
                        operator_branches b ON b.id = loc.branch_id
                            LEFT JOIN
                        operator_stores s ON s.id = loc.store_id
                    """;
        } else {
            fromStatement = """
                    FROM oem_client_accounts oca
                            JOIN
                        accounts a ON a.id = oca.id
                            LEFT JOIN
                        (SELECT
                            m.oem_client_id AS managerId,
                                GROUP_CONCAT(a.client_name) AS managerNames
                        FROM
                            oem_client_managers m
                        JOIN oem_client_accounts a ON a.id = m.oem_client_manager_id
                        GROUP BY m.oem_client_id) AS manager_tbl ON manager_tbl.managerId = oca.id
                            LEFT JOIN
                        oem_client_locations loc ON loc.oem_client_id = oca.id
                            LEFT JOIN
                        oem_branches b ON b.id = loc.branch_id
                            LEFT JOIN
                        oem_stores s ON s.id = loc.store_id
                    """;
        }
        return fromStatement;
    }

    private long countQuery(String unionStatement, String oemGroupId, String oemAccountId, String searchInput) {
        String countSelectQuery = String.format("SELECT COUNT(DISTINCT temp.id) FROM (%s) AS temp", unionStatement);
        Query countQuery = entityManager.createNativeQuery(countSelectQuery, Long.class);
        countQuery.setParameter("oemGroupId", oemGroupId);
        countQuery.setParameter("oemAccountId", oemAccountId);
        countQuery.setParameter("searchInput", GeneralUtils.wrapToLike(searchInput));

        return Objects.nonNull(countQuery.getSingleResult()) ? (long) countQuery.getSingleResult() : 0;
    }
}
