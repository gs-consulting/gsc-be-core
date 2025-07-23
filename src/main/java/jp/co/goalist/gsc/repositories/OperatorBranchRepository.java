package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorBranchRepository extends JpaRepository<OperatorBranch, String> {

    @Query(value = """
            from operator_branches b left join prefectures p on b.prefecture.id = p.id
            left join cities c on b.city.id = c.id
            where (:searchInput is null or b.branchName like :searchInput
            or b.branchCode like :searchInput or p.name like :searchInput
            or c.name like :searchInput)""")
    Page<OperatorBranch> findParentOperatorBranchBy(String searchInput, Pageable pageable);

    @Query(value = """
            from operator_branches b
            where (case when (:parentId is null) then (b.parentId is null) else (b.parentId is null or b.parentId = :parentId) end)""")
    Page<OperatorBranch> findOperatorBranchBy(String parentId, Pageable pageable);

    @Query(value = """
            from operator_branches where id in (
            select b.id from operator_branches b left join prefectures p on b.prefecture.id = p.id
            left join cities c on b.city.id = c.id
            where
            (b.parentId is null or b.parentId = :clientAccountId)
            and (:searchInput is null or b.branchName like :searchInput
            or b.branchCode like :searchInput or p.name like :searchInput
            or c.name like :searchInput))
            order by createdAt asc""")
    Page<OperatorBranch> findOperatorClientBranchBy(String clientAccountId, String searchInput, Pageable pageable);

    @Query(value = """
        SELECT b FROM operator_branches b WHERE b.id IN (:ids)
        AND (b.parentId IS NULL OR b.parentId = :clientAccountId)
        """)
    List<OperatorBranch> findBranchesBy(List<String> ids, String clientAccountId);
}
