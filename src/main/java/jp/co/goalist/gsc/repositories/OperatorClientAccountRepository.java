package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorClientAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorClientAccountRepository extends JpaRepository<OperatorClientAccount, String> {

    @Query(value = """
                from operator_client_accounts o join accounts a on o.id = a.id
                where a.enabled = true and o.id in (:ids)
                order by a.id asc
            """)
    List<OperatorClientAccount> findAllEnabledAccountsByIds(List<String> ids);

    @Query(value = """
                FROM operator_client_accounts a
                WHERE (:searchInput is null or a.clientName like :searchInput)
                and (CASE WHEN (:clientAccountId is null) THEN (a.parent.id IS NULL) ELSE (a.parent.id = :clientAccountId) END)
                order by a.createdAt asc
            """)
    Page<OperatorClientAccount> findAllBy(String clientAccountId, String searchInput, Pageable pageable);

    @Query(value = """
                FROM operator_client_accounts a
                WHERE a.parent.id = :parentId
                AND (:searchInput IS NULL OR a.clientName LIKE :searchInput)
                ORDER BY a.createdAt ASC
            """)
    Page<OperatorClientAccount> findAllInternalUsersBy(String parentId, String searchInput, Pageable pageable);

    @Query(value = """
                from operator_client_accounts a
                where a.id in (:ids)
                order by a.createdAt asc
            """)
    List<OperatorClientAccount> findAllByIds(List<String> ids);

    @Query(value = """
            select * from operator_client_accounts where id in (
            select o.id from operator_client_accounts o 
            join accounts a on o.id = a.id
            left join operator_client_locations obs on obs.operator_client_id = o.id
            left join operator_client_managers m on m.operator_client_id = o.id
            where a.enabled = true
            and (:managerId is null or o.parent_id = :managerId)
            and (:branchId is null or obs.branch_id = :branchId)
            and (:storeId is null or obs.store_id = :storeId)) 
            order by created_at asc""",
            countQuery = """
                    select count(*) from operator_client_accounts where id in (
                    select o.id from operator_client_accounts o
                        join accounts a on o.id = a.id
                        left join operator_client_locations obs on obs.operator_client_id = o.id
                        left join operator_client_managers m on m.operator_client_id = o.id
                        where a.enabled = true
                        and (:managerId is null or o.parent_id = :managerId)
                        and (:branchId is null or obs.branch_id = :branchId)
                        and (:storeId is null or obs.store_id = :storeId))""",
            nativeQuery = true)
    Page<OperatorClientAccount> findAllEnabledAccounts(String managerId, String branchId, String storeId, Pageable pageable);

    @Query(value = """
                from operator_client_accounts a join accounts acc on a.id = acc.id
                where (:parentId is null or a.parent.id = :parentId)
                    and (case when (:isInterviewer is true) then (a.isInterviewer = true) else (1=1) end)
                    and acc.enabled = true
                order by a.createdAt asc
            """)
    List<OperatorClientAccount> findAllForDropdown(String parentId, Boolean isInterviewer);

    @Query(value = "from oem_client_accounts a where a.domainSetting = :domainSetting")
    Optional<OperatorClientAccount> findParentClientDomain(String domainSetting);
}
