package jp.co.goalist.gsc.repositories;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.entities.OemTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemTeamRepository extends JpaRepository<OemTeam, String> {

    @Query(value = """
            select t from oem_teams t
            where t.oemParent.id = :oemId
            and (:searchInput is null or t.name like :searchInput)
            order by t.createdAt asc
            """)
    @QueryHints(value = {
            @QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "USE"),
            @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<OemTeam> findAllBy(String oemId, String searchInput, Pageable pageable);

    @Query(value = """
            select t from oem_teams t
            where (:oemParentId is null or t.oemParent.id = :oemParentId)
            and (:oemGroupId is null or t.oemGroup.id = :oemGroupId)
            order by t.createdAt asc
            """)
    Page<OemTeam> findAllByOemId(String oemGroupId, String oemParentId, Pageable pageable);

    @Query(value = """
        from oem_teams where (:id is null or id <> :id) and name = :name and oemParent.id = :oemParentId""")
    Optional<OemTeam> findDuplicateBy(String oemParentId, String name, String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from oem_account_teams where team_id in (:ids)", nativeQuery = true)
    void deleteOemAcountTeamsByParentAndIdIn(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from oem_teams where id in (:ids)", nativeQuery = true)
    void deleteTeamsByParentAndIdIn(List<String> ids);
}
