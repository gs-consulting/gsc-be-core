package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.ProjectInfoByFlow;
import jp.co.goalist.gsc.entities.OperatorProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorProjectRepository extends JpaRepository<OperatorProject, String> {
    @Query("""
                SELECT p FROM operator_projects p WHERE p.id IN :ids
                AND p.parentId = :parentId
                AND (case when (:branchId is null) then (p.branch is null) else (p.branch.id = :branchId) end)
            """)
    List<OperatorProject> findAllProjectsByIdsAndBranchId(List<String> ids, String parentId, String branchId);

    @Query("SELECT p FROM operator_projects p WHERE p.parentId = :parentId")
    List<OperatorProject> findAllProjectsByParentId(String parentId);

    @Query(value = """
                SELECT p FROM operator_projects p WHERE
                p.store.id = :storeId
                ORDER BY p.createdAt desc
            """)
    Page<OperatorProject> findAllProjectsByStoreId(String storeId, Pageable pageable);

    @Query(value = """
                SELECT p.id, p.project_number as projectNo, p.project_name, st.status_name, occ.status_name as occupation,
                       p.goal_apply, p.goal_interview, p.goal_offer, p.goal_agreement,
                       GROUP_CONCAT(q.status_name SEPARATOR ',') as qualifications,
                       CONCAT(IFNULL(pr.name, ''), " ", IFNULL(c.name, '')) as workplace,
                       p.ab_adjustment, p.memo, p.created_at as registeredDate
                FROM operator_projects p  
                LEFT JOIN prefectures pr on p.prefecture_id = pr.id
                LEFT JOIN cities c on p.city_id = c.id
                LEFT JOIN master_statuses st on p.status_id = st.id 
                LEFT JOIN master_statuses occ on p.occupation_id = occ.id
                LEFT JOIN master_statuses q ON FIND_IN_SET(q.id, p.qualifications) > 0
                WHERE p.id = :id and p.parent_id = :parentId
                GROUP BY p.id, pr.name, c.name
            """, nativeQuery = true)
    ProjectInfoByFlow findProjectById(String id, String parentId);

    @Query("SELECT p FROM operator_projects p WHERE p.projectNumber in (:projectNos) and p.parentId = :parentId")
    List<OperatorProject> findAllProjectsByProjectNo(List<String> projectNos, String parentId);
}