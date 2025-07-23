package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemClientAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemClientAccountRepository extends JpaRepository<OemClientAccount, String> {

    @Query(value = """
                FROM oem_client_accounts o JOIN accounts a ON o.id = a.id
                WHERE a.enabled = TRUE AND o.id IN (:ids)
                ORDER BY a.id ASC
            """)
    List<OemClientAccount> findAllEnabledAccountsByIds(List<String> ids);

    @Query(value = """
                FROM oem_client_accounts a
                WHERE a.id IN (:ids)
                ORDER BY a.createdAt ASC
            """)
    List<OemClientAccount> findAllByIds(List<String> ids);

    @Query(value = """
                FROM oem_client_accounts a
                WHERE a.oemGroupId = :oemGroupId AND a.parent.id = :parentId
                AND (:searchInput IS NULL OR a.clientName LIKE :searchInput)
                ORDER BY a.createdAt ASC
            """)
    Page<OemClientAccount> findAllInternalUsersBy(String parentId, String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
                SELECT DISTINCT o.* FROM oem_client_accounts o 
                JOIN accounts a ON o.id = a.id
                LEFT JOIN oem_client_locations obs ON obs.oem_client_id = o.id
                LEFT JOIN oem_client_managers m ON m.oem_client_id = o.id
                WHERE (:managerId IS null OR o.parent_id = :managerId)
                AND (:branchId IS null OR obs.branch_id = :branchId)
                AND (:storeId IS null OR obs.store_id = :storeId)
                AND a.enabled = TRUE
                ORDER BY created_at ASC""",
            countQuery = """
                    SELECT count(DISTINCT o.*) FROM oem_client_accounts o
                    JOIN accounts a ON o.id = a.id
                    LEFT JOIN oem_client_locations obs ON obs.oem_client_id = o.id
                    LEFT JOIN oem_client_managers m ON m.oem_client_id = o.id
                    WHERE (:managerId IS null OR o.parent_id = :managerId)
                    AND (:branchId IS null OR obs.branch_id = :branchId)
                    AND (:storeId IS null OR obs.store_id = :storeId)
                    AND a.enabled = TRUE""",
            nativeQuery = true)
    Page<OemClientAccount> findAllOemClientAccountsDropdown(String managerId, String branchId, String storeId, Pageable pageable);

    @Query(value = """
                FROM oem_client_accounts a JOIN accounts acc ON a.id = acc.id
                WHERE a.oemGroupId = :oemGroupId
                    AND (:parentId IS null OR a.parent.id = :parentId)
                    AND (case when (:isInterviewer IS TRUE) then (a.isInterviewer = TRUE) ELSE (1=1) END)
                    AND acc.enabled = TRUE
                ORDER BY a.createdAt ASC
            """)
    List<OemClientAccount> findAllForDropdown(String parentId, String oemGroupId, Boolean isInterviewer);

    @Query(value = "FROM oem_client_accounts a WHERE a.domainSetting = :domainSetting")
    Optional<OemClientAccount> findParentClientDomain(String domainSetting);
}
