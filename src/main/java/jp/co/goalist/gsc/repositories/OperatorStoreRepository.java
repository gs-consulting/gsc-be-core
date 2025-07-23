package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorStoreRepository extends JpaRepository<OperatorStore, String> {

    @Query(value = """
            SELECT * FROM operator_stores WHERE id IN (
            SELECT s.id FROM operator_stores s LEFT JOIN prefectures p ON s.prefecture_id = p.id
            LEFT JOIN cities c ON s.city_id = c.id
            LEFT JOIN operator_branches b ON s.branch_id = b.id
            WHERE (CASE WHEN :clientAccountId IS NULL THEN 1=1 ELSE s.parent_id = :clientAccountId OR s.parent_id IS NULL END)
            AND (:searchInput IS NULL OR s.store_name LIKE :searchInput
            OR s.store_code LIKE :searchInput OR p.name LIKE :searchInput
            OR c.name LIKE :searchInput
            OR b.branch_name LIKE :searchInput))
            AND (:branchId IS NULL OR s.branch_id = :branchId)
            ORDER BY s.created_at ASC""",
            nativeQuery = true)
    Page<OperatorStore> findOperatorStoresBy(String clientAccountId, String branchId, String searchInput, Pageable pageable);

    @Query(value = """
        SELECT s.* FROM operator_stores s WHERE
        s.branch_id IN (:branchIds)
        AND s.id IN (:ids)
        ORDER BY s.created_at ASC
    """, nativeQuery = true)
    List<OperatorStore> getOperatorStoresByIdsAndBranchIds(List<String> ids, List<String> branchIds);

    @Query(value = """
        SELECT s.* FROM operator_stores s 
        WHERE s.branch_id IN (:branchIds)
        AND (CASE WHEN :clientAccountId IS NULL
            THEN (CASE WHEN :isAll = 1 THEN 1=1 ELSE s.parent_id IS NULL END)
            ELSE (s.parent_id IS NULL OR s.parent_id = :clientAccountId) END)
        ORDER BY s.created_at ASC
    """, nativeQuery = true)
    Page<OperatorStore> findAllStoresDropdownByBranchIds(String clientAccountId, Integer isAll, List<String> branchIds, Pageable pageable);

    @Query(value = """
        SELECT * FROM operator_stores
        WHERE (COALESCE(:branchIds) IS NULL OR t.branch_id IN (:branchIds))
        AND (o.parent_id IS NULL OR (:clientAccountId IS NULL OR o.parent_id = :clientAccountId))
        ORDER BY s.created_at ASC
    """, nativeQuery = true)
    List<OperatorStore> findClientStoresByBranchIds(String clientAccountId, List<String> branchIds);

    @Query(value = """
        SELECT s FROM operator_stores s WHERE s.id IN (:ids)
        AND (s.parentId IS NULL OR s.parentId = :clientAccountId)""")
    List<OperatorStore> findStoresBy(List<String> ids, String clientAccountId);
}
