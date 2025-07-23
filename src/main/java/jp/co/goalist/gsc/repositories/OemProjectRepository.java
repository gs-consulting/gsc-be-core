package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.ProjectInfoByFlow;
import jp.co.goalist.gsc.entities.OemProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemProjectRepository extends JpaRepository<OemProject, String> {

    @Query("SELECT p FROM oem_projects p WHERE p.parentId = :parentId AND p.oemGroupId = :oemGroupId")
    List<OemProject> findAllProjectsByParentIdoemGroupId(String parentId, String oemGroupId);

    @Query("""
                SELECT p FROM oem_projects p WHERE p.id IN :ids
                AND p.parentId = :parentId
                AND (case when (:branchId is null) then (p.branch is null) else (p.branch.id = :branchId) end)
            """)
    List<OemProject> findAllProjectsByIdsAndBranchId(List<String> ids, String parentId, String branchId);

    @Query("""
                SELECT p FROM oem_projects p WHERE
                p.oemGroupId = :oemGroupId AND p.store.id = :storeId
                ORDER BY p.createdAt desc
            """)
    Page<OemProject> findAllProjectsByStoreId(String oemGroupId, String storeId, Pageable pageable);

    @Query(value = """
                SELECT p.id, p.project_number as projectNo, p.project_name, st.status_name, occ.status_name as occupation,
                       p.goal_apply, p.goal_interview, p.goal_offer, p.goal_agreement,
                       GROUP_CONCAT(q.status_name SEPARATOR ',') as qualifications,
                       CONCAT(IFNULL(pr.name, ''), " ", IFNULL(c.name, '')) as workplace,
                       p.ab_adjustment, p.memo, p.created_at as registeredDate
                FROM oem_projects p  
                LEFT JOIN prefectures pr on p.prefecture_id = pr.id
                LEFT JOIN cities c on p.city_id = c.id
                LEFT JOIN master_statuses st on p.status_id = st.id 
                LEFT JOIN master_statuses occ on p.occupation_id = occ.id
                LEFT JOIN master_statuses q ON FIND_IN_SET(q.id, p.qualifications) > 0
                WHERE p.id = :id and p.parent_id = :parentId and p.oem_group_id = :oemGroupId
                GROUP BY p.id, pr.name, c.name
            """, nativeQuery = true)
    ProjectInfoByFlow findProjectById(String id, String parentId, String oemGroupId);

    @Query("SELECT p FROM oem_projects p WHERE p.projectNumber in (:projectNos) and p.parentId = :parentId and p.oemGroupId = :oemGroupId")
    List<OemProject> findAllProjectsByProjectNo(List<String> projectNos, String parentId, String oemGroupId);
}