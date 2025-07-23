package jp.co.goalist.gsc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OperatorAdvertisement;

@Repository
public interface OperatorAdvertisementRepository extends JpaRepository<OperatorAdvertisement, String> {

    @Query("""
            select oa from operator_advertisements oa
            left join operator_advertisement_linkings oal on oa.id = oal.advertisementId
            where (oal.projectId = :projectId or oa.linkingType = 'ALL') and oa.parentId = :parentId
            """)
    Page<OperatorAdvertisement> findLinkedAdvertisements(String projectId, String parentId, Pageable pageable);

    @Query("""
            select oa from operator_advertisements oa
            where oa.name in (:names) and oa.parentId = :parentId
            """)
    List<OperatorAdvertisement> findAdvertisementsBy(List<String> names, String parentId);

    @Query("""
        SELECT a.masterMedia.id
        FROM operator_advertisements a
        WHERE a.parentId = :parentId AND a.linkingType = 'ALL' GROUP BY a.masterMedia.id""")
    List<String> checkHasAdvertisementWithAllProjects(String parentId);
}
