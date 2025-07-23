package jp.co.goalist.gsc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.entities.OemAccount;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemAccountRepository extends JpaRepository<OemAccount, String> {

    @Query(value = """
            from oem_accounts a join oem_groups c on a.oemGroup.id = c.id
            where a.id = :id and c.id = :oemGroupId""")
    Optional<OemAccount> findByIdAndOemGroupId(String id, String oemGroupId);

    @Query(value = """
        select a from oem_accounts a
        where a.oemGroup.id = :oemGroupId and a.parent is null
        and (:searchInput is null or  a.fullName like :searchInput)
        order by a.id asc
    """)
    Page<OemAccount> findAllParentOemBy(String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
        select a from oem_accounts a
        where a.oemGroup.id = :oemGroupId and
        (:oemId is null or a.parent.id = :oemId or a.id = :oemId) and
        (:searchInput is null or  a.fullName like :searchInput)
        order by a.id asc
    """)
    Page<OemAccount> findAllBy(String oemId, String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
        select o.* from oem_accounts o join accounts a on o.id = a.id
        left join oem_account_teams ot on o.id = ot.oem_id
        where (case when :teamId is null then a.enabled = true else ot.team_id = :teamId end)
        and (o.id = :oemParentId or o.parent_id = :oemParentId)
        order by a.created_at asc
    """, nativeQuery = true)
    Page<OemAccount> findAllEnabledManagersDropdown(String oemParentId, String teamId, Pageable pageable);

    @Query(value = """
        select o.* from oem_accounts o join accounts a on o.id = a.id
        left join oem_account_teams ot on o.id = ot.oem_id
        where (case when :teamId is null then a.enabled = true else ot.team_id = :teamId end)
        and (o.id = :oemParentId or o.parent_id = :oemParentId)
        order by a.created_at asc
            """, nativeQuery = true)
    List<OemAccount> findAllByTeam(String oemParentId, String teamId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from oem_account_teams where team_id in (:ids)", nativeQuery = true)
    void deleteOemAcountTeamsByIdIn(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from oem_accounts where parent_id = :parentId and oem_group_id = :oemGroupId and id in (:ids)", nativeQuery = true)
    void deleteSelectedOemStaffsByParentAndIdIn(List<String> ids, String parentId, String oemGroupId);
}
