package jp.co.goalist.gsc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OemAdvertisement;

@Repository
public interface OemAdvertisementRepository extends JpaRepository<OemAdvertisement, String> {

    @Query("""
            select oa from oem_advertisements oa
            left join oem_advertisement_linkings oal on oa.id = oal.advertisementId
            where (oal.projectId = :projectId or oa.linkingType = 'ALL') and oa.parentId = :parentId
            """)
    Page<OemAdvertisement> findLinkedAdvertisements(String projectId, String parentId, Pageable pageable);

    @Query("""
            select oa from oem_advertisements oa
            where oa.name in (:names) and oa.parentId = :parentId
            """)
    List<OemAdvertisement> findAdvertisementsBy(List<String> names, String parentId, String oemGroupId);

    @Query("""
        SELECT a.masterMedia.id
        FROM oem_advertisements a WHERE a.parentId = :parentId and a.oemGroupId = :oemGroupId AND a.linkingType = 'ALL'
        GROUP BY a.masterMedia.id""")
    List<String> checkHasAdvertisementWithAllProjects(String parentId, String oemGroupId);

}
