package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.ProjectMediaDto;
import jp.co.goalist.gsc.entities.MasterMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterMediaRepository extends JpaRepository<MasterMedia, String> {

    @Query("""
        select m from master_medias m
        where (:searchInput is null or m.mediaName like :searchInput)
        and m.parentId = :parentId
        and (case when (:oemGroupId is null) then (m.oemGroupId is null) else (m.oemGroupId = :oemGroupId) end)
        """)
    Page<MasterMedia> findAllBy(String parentId, String oemGroupId, String searchInput, Pageable pageable);

    Optional<MasterMedia> findByIdAndParentId(String id, String parentId);

    @Query("""
        select m from master_medias m where m.id in (:ids) and m.parentId = :parentId
        and (case when (:oemGroupId is null) then (m.oemGroupId is null) else (m.oemGroupId = :oemGroupId) end)
        """)
    List<MasterMedia> findAllByIDs(List<String> ids, String parentId, String oemGroupId);

    @Query("from master_medias m where m.parentId = :parentId and ((:oemGroupId is null and m.oemGroupId is null) or m.oemGroupId = :oemGroupId) and m.mediaName = :mediaName")
    Optional<MasterMedia> findByMediaName(String mediaName, String parentId, String oemGroupId);

    @Query(value = """
        SELECT DISTINCT m.* FROM master_medias m LEFT JOIN operator_advertisements a ON a.master_media_id = m.id
        LEFT JOIN (SELECT li.advertisement_id, GROUP_CONCAT(li.project_id) AS projectIds
                   FROM operator_advertisement_linkings li GROUP BY li.advertisement_id) AS links ON a.id  = links.advertisement_id
        WHERE 
            (
                m.parent_id = :parentId
                AND (:projectId IS NULL OR (FIND_IN_SET(:projectId, links.projectIds) > 0 OR a.linking_type = 'ALL'))
            )
            OR
            (:applicantId IS NOT NULL AND m.id IN (SELECT media_id FROM operator_applicants WHERE id = :applicantId))  
        """, nativeQuery = true)
    List<MasterMedia> findAllOperatorForDropdown(String parentId, String projectId, String applicantId);

    @Query(value = """
        SELECT DISTINCT m.* FROM master_medias m LEFT JOIN oem_advertisements a ON a.master_media_id = m.id
        LEFT JOIN (SELECT li.advertisement_id, GROUP_CONCAT(li.project_id) AS projectIds
                   FROM oem_advertisement_linkings li GROUP BY li.advertisement_id) AS links ON a.id  = links.advertisement_id
        WHERE
            (
                m.parent_id = :parentId
                AND m.oem_group_id = :oemGroupId
                AND (:projectId IS NULL OR (FIND_IN_SET(:projectId, links.projectIds) > 0 OR a.linking_type = 'ALL'))
            )
            OR
            (:applicantId IS NOT NULL AND m.id IN (SELECT media_id FROM oem_applicants WHERE id = :applicantId))  
        """, nativeQuery = true)
    List<MasterMedia> findAllOemForDropdown(String parentId, String oemGroupId, String projectId, String applicantId);

    @Query("""
        SELECT new jp.co.goalist.gsc.dtos.ProjectMediaDto(p.projectNumber, m.id)
        FROM master_medias m
        JOIN operator_advertisements a ON m.id = a.masterMedia.id
        JOIN operator_advertisement_linkings l ON l.advertisementId = a.id
        JOIN operator_projects p ON p.id = l.projectId
        WHERE m.id IN (:ids)
        AND m.parentId = :parentId
        """)
    List<ProjectMediaDto> findOperatorMasterMediaByAdvertisement(List<String> ids, String parentId);

    @Query("""
        SELECT new jp.co.goalist.gsc.dtos.ProjectMediaDto(p.projectNumber, m.id)
        FROM master_medias m
        JOIN oem_advertisements a ON m.id = a.masterMedia.id
        JOIN oem_advertisement_linkings l ON l.advertisementId = a.id
        JOIN oem_projects p ON p.id = l.projectId
        WHERE m.id IN (:ids)
        AND m.parentId = :parentId AND m.oemGroupId = :oemGroupId
        """)
    List<ProjectMediaDto> findOemMasterMediaByAdvertisement(List<String> ids, String parentId, String oemGroupId);
}
