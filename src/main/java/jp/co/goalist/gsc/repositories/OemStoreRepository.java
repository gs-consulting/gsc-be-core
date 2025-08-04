package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemStoreRepository extends JpaRepository<OemStore, String> {

    @Query(value = """
        SELECT s.* FROM oem_stores s WHERE 
        s.branch_id IN (:branchIds)
        AND s.id IN (:ids)
    """, nativeQuery = true)
    List<OemStore> getOemStoresByIdsAndBranchIds(List<String> ids, List<String> branchIds);

    @Query(value = """
            SELECT * FROM oem_stores WHERE id in (SELECT s.id FROM oem_stores s LEFT JOIN prefectures p ON s.prefecture_id = p.id
            LEFT JOIN cities c ON s.city_id = c.id
            LEFT JOIN oem_branches b ON s.branch_id = b.id
            WHERE s.oem_group_id = :oemGroupId
            AND s.oem_parent_id = :oemParentId
            AND (s.parent_id IS NULL OR s.parent_id = :parentId)
            AND (:searchInput IS NULL OR s.store_name LIKE :searchInput
            OR s.store_code LIKE :searchInput OR p.name LIKE :searchInput
            OR c.name LIKE :searchInput
            OR b.branch_name LIKE :searchInput)
            AND (:branchId IS NULL OR s.branch_id = :branchId))
            ORDER BY created_at asc""",
        nativeQuery = true,
        countQuery = """
            SELECT count(*) FROM oem_stores WHERE id IN (SELECT s.id FROM oem_stores s LEFT JOIN prefectures p ON s.prefecture_id = p.id
            LEFT JOIN cities c ON s.city_id = c.id
            LEFT JOIN oem_branches b ON s.branch_id = b.id
            WHERE s.oem_group_id = :oemGroupId
            AND s.oem_parent_id = :oemParentId
            AND (s.parent_id IS NULL OR s.parent_id = :parentId)
            AND (:searchInput IS NULL OR s.store_name LIKE :searchInput
            OR s.store_code LIKE :searchInput OR p.name LIKE :searchInput
            OR c.name LIKE :searchInput
            OR b.branch_name LIKE :searchInput)
            AND (:branchId IS NULL OR s.branch_id = :branchId))
            """)
    Page<OemStore> findOemClientStoresBy(String parentId, String oemParentId, String oemGroupId,
                                         String branchId, String searchInput, Pageable pageable);

    @Query(value = """
        SELECT s.* FROM oem_stores s WHERE
        (COALESCE(:branchIds) IS NULL OR s.branch_id IN (:branchIds))
        AND (CASE WHEN :clientAccountId IS NULL 
        THEN (CASE WHEN :isAll = 1 THEN s.oem_parent_id = :oemParentId ELSE s.parent_id IS NULL END)
        ELSE (s.parent_id IS NULL OR s.parent_id = :clientAccountId) END)
        ORDER BY s.created_at ASC
    """, nativeQuery = true)
    Page<OemStore> findAllStoresDropdownByBranchId(String oemParentId,
                                                   String clientAccountId,
                                                   Integer isAll,
                                                   List<String> branchIds,
                                                   Pageable pageable);

    @Query(value = """
        SELECT s.* FROM oem_stores s WHERE 
        s.oem_parent_id = :oemParentId
        AND (:clientAccountId IS NULL OR s.parent_id = :clientAccountId OR s.parent_id IS NULL)
        AND (coalesce(:branchIds) IS NULL OR s.branch_id IN (:branchIds))
        ORDER BY s.created_at ASC
    """, nativeQuery = true)
    List<OemStore> findClientStoresDropdownByBranchIds(String clientAccountId, String oemParentId, List<String> branchIds);

    @Query(value = """
        SELECT s FROM oem_stores s WHERE s.id IN (:ids)
        AND (s.parentId IS NULL OR s.parentId = :parentId)
        AND s.oemParentId = :oemParentId
        AND s.oemGroupId = :oemGroupId""")
    List<OemStore> findStoresBy(List<String> ids, String parentId, String oemParentId, String oemGroupId);

    @Query(value = """
            SELECT s FROM oem_stores s WHERE s.id IN (:ids)
            AND (s.parentId IS NULL OR s.parentId = :parentId)
            AND s.oemGroupId = :oemGroupId""")
    List<OemStore> findStoresBy(List<String> ids, String parentId, String oemGroupId);

    @Modifying
    @Query(value = "DELETE FROM oem_stores WHERE branch_id IN (:branchIds) AND parent_id = :parentId AND oem_group_id = :oemGroupId", nativeQuery = true)
    void deleteStoresByBranchIds(List<String> branchIds, String parentId, String oemGroupId);

    @Modifying
    @Query(value = "DELETE FROM oem_stores WHERE id IN (:storeIds) AND parent_id = :parentId AND oem_group_id = :oemGroupId", nativeQuery = true)
    void deleteStoresByIds(List<String> storeIds, String parentId, String oemGroupId);
}
