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
            FROM oem_accounts a JOIN oem_groups c ON a.oemGroup.id = c.id
            WHERE a.id = :id AND c.id = :oemGroupId""")
    Optional<OemAccount> findByIdAndOemGroupId(String id, String oemGroupId);

    @Query(value = """
        SELECT a FROM oem_accounts a
        WHERE a.oemGroup.id = :oemGroupId AND a.parent IS NULL
        AND (:searchInput IS NULL OR  a.fullName LIKE :searchInput)
        ORDER BY a.id ASC
    """)
    Page<OemAccount> findAllParentOemBy(String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
            SELECT oc.* FROM oem_accounts oc WHERE oc.id IN 
                (SELECT o.id
                    FROM oem_accounts o
                    JOIN accounts a ON o.id = a.id
                    LEFT JOIN oem_account_teams ot ON a.id = ot.oem_id
                    LEFT JOIN oem_teams t ON ot.team_id = t.id
                    WHERE o.oem_group_id = :oemGroupId AND
                        (:oemId IS NULL OR o.parent_id = :oemId OR o.id = :oemId) AND
                        (:searchInput IS NULL OR  o.full_name LIKE :searchInput OR t.name LIKE :searchInput) AND
                        a.is_deleted = false)
            order by oc.created_at asc
        """,
        countQuery = """
            SELECT oc.* FROM oem_accounts oc WHERE oc.id IN 
                (SELECT o.id
                    FROM oem_accounts o
                    JOIN accounts a ON o.id = a.id
                    LEFT JOIN oem_account_teams ot ON a.id = ot.oem_id
                    LEFT JOIN oem_teams t ON ot.team_id = t.id
                    WHERE o.oem_group_id = :oemGroupId AND
                        (:oemId IS NULL OR o.parent_id = :oemId OR o.id = :oemId) AND
                        (:searchInput IS NULL OR  o.full_name LIKE :searchInput OR t.name LIKE :searchInput) AND
                        a.is_deleted = false)
            order by oc.created_at asc
                """, nativeQuery = true)
    Page<OemAccount> findAllBy(String oemId, String oemGroupId, String searchInput, Pageable pageable);

    @Query(value = """
        SELECT o.* FROM oem_accounts o JOIN accounts a ON o.id = a.id
        LEFT JOIN oem_account_teams ot ON o.id = ot.oem_id
        WHERE (CASE WHEN :teamId IS NULL THEN a.enabled = true ELSE ot.team_id = :teamId END)
        AND (o.id = :oemParentId OR o.parent_id = :oemParentId)
        ORDER BY a.created_at ASC
    """, nativeQuery = true)
    Page<OemAccount> findAllEnabledManagersDropdown(String oemParentId, String teamId, Pageable pageable);

    @Query(value = """
        SELECT o.* FROM oem_accounts o JOIN accounts a ON o.id = a.id
        LEFT JOIN oem_account_teams ot ON o.id = ot.oem_id
        WHERE (CASE WHEN :teamId IS NULL THEN a.enabled = true ELSE ot.team_id = :teamId END)
        AND (o.id = :oemParentId OR o.parent_id = :oemParentId)
        ORDER BY a.created_at ASC
            """, nativeQuery = true)
    List<OemAccount> findAllByTeam(String oemParentId, String teamId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete FROM oem_account_teams WHERE oem_id in (:ids)", nativeQuery = true)
    void deleteOemAcountTeamsByIdIn(List<String> ids);
}
