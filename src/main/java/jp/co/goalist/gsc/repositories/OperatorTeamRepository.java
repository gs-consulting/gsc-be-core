package jp.co.goalist.gsc.repositories;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.entities.OperatorTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorTeamRepository extends JpaRepository<OperatorTeam, String> {

    @Query(value = """
            select t from operator_teams t
            where (:searchInput is null or t.name like :searchInput)
            order by t.createdAt asc
            """)
    @QueryHints(value = {
            @QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "USE"),
            @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<OperatorTeam> findAllBy(@Param("searchInput") String searchInput, Pageable pageable);

    @Query(value = """
        from operator_teams where (:id is null or id <> :id) and name = :name""")
    Optional<OperatorTeam> findDuplicateBy(String name, String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from operator_account_teams where team_id in (:ids)", nativeQuery = true)
    void deleteOpAcountTeamsByParentAndIdIn(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from operator_teams where id in (:ids)", nativeQuery = true)
    void deleteTeamsByParentAndIdIn(List<String> ids);
}