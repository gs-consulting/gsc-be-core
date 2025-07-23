package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemBranchRepository extends JpaRepository<OemBranch, String> {

    @Query(value = """
            from oem_branches b left join prefectures p on b.prefecture.id = p.id
            left join cities c on b.city.id = c.id
            where b.oemGroupId = :oemGroupId and b.oemParentId = :oemParentId
            and (:searchInput is null or b.branchName like :searchInput
            or b.branchCode like :searchInput or p.name like :searchInput
            or c.name like :searchInput)
            order by b.createdAt asc""")
    Page<OemBranch> findParentOemBranchBy(String oemGroupId, String oemParentId, String searchInput, Pageable pageable);

    @Query(value = """
            from oem_branches b left join prefectures p on b.prefecture.id = p.id
            left join cities c on b.city.id = c.id
            where b.oemGroupId = :oemGroupId and b.oemParentId = :oemParentId
            and (case when (:parentId is null) then (b.parentId is null) else (b.parentId is null or b.parentId = :parentId) end)
            and (:searchInput is null or b.branchName like :searchInput
            or b.branchCode like :searchInput or p.name like :searchInput
            or c.name like :searchInput)
            order by b.createdAt asc""")
    Page<OemBranch> findOemBranchBy(String oemGroupId, String oemParentId, String parentId, String searchInput, Pageable pageable);

    @Query(value = """
            FROM oem_branches WHERE id IN(
            SELECT b.id FROM oem_branches b LEFT JOIN prefectures p ON b.prefecture.id = p.id
            LEFT JOIN cities c ON b.city.id = c.id
            WHERE (b.parentId IS NULL OR b.parentId = :parentId)
            AND b.oemParentId = :oemParentId
            AND b.oemGroupId = :oemGroupId
            AND (:searchInput IS NULL OR b.branchName LIKE :searchInput
            OR b.branchCode LIKE :searchInput OR p.name LIKE :searchInput
            OR c.name LIKE :searchInput))
            ORDER BY createdAt ASC""")
    Page<OemBranch> findOemClientBranchBy(String parentId, String oemParentId, String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
            SELECT b FROM oem_branches b WHERE b.id IN (:ids)
            AND (b.parentId IS NULL OR b.parentId = :parentId)
            AND b.oemParentId = :oemParentId
            AND b.oemGroupId = :oemGroupId
            """)
    List<OemBranch> findBranchesBy(List<String> ids, String parentId, String oemParentId, String oemGroupId);
}
