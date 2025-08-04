package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorAccount;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OperatorAccountRepository extends JpaRepository<OperatorAccount, String> {

    @Query(value = """
                SELECT o.* FROM operator_accounts o 
                JOIN accounts a ON o.id = a.id 
                LEFT JOIN operator_account_teams ot ON o.id = ot.operator_id
                WHERE (CASE WHEN :teamId IS NULL THEN a.enabled = true ELSE ot.team_id = :teamId END)
                ORDER BY a.created_at ASC
            """, nativeQuery = true)
    Page<OperatorAccount> findAllEnabledManagersDropdown(Pageable pageable, String teamId);

    @Query(value = """
                SELECT oc.* FROM operator_accounts oc WHERE oc.id in 
                    (SELECT o.id
                        FROM operator_accounts o
                        JOIN accounts a ON o.id = a.id
                        LEFT JOIN operator_account_teams ot ON a.id = ot.operator_id
                        LEFT JOIN operator_teams t ON ot.team_id = t.id
                        WHERE (:searchInput IS NULL OR o.full_name LIKE :searchInput
                        OR a.email LIKE :searchInput OR t.name LIKE :searchInput)
                        AND a.is_deleted = false)
                ORDER BY oc.created_at ASC
            """,
            countQuery = """
                    SELECT count(oc.*) FROM operator_accounts oc WHERE oc.id in
                        (SELECT o.id
                            FROM operator_accounts o
                            JOIN accounts a ON o.id = a.id
                            LEFT JOIN operator_account_teams ot ON a.id = ot.operator_id
                            LEFT JOIN operator_teams t ON ot.team_id = t.id
                            WHERE (:searchInput IS NULL OR o.full_name LIKE :searchInput
                            OR a.email LIKE :searchInput OR t.name LIKE :searchInput)
                            AND a.is_deleted = false)
                    ORDER BY oc.created_at ASC
                    """, nativeQuery = true)
    Page<OperatorAccount> findAllBy(String searchInput, Pageable pageable);
    
    @Query(value = """
                SELECT o.* FROM operator_accounts o 
                JOIN accounts a ON o.id = a.id 
                LEFT JOIN operator_account_teams ot ON o.id = ot.operator_id
                WHERE (CASE WHEN :teamId IS NULL THEN a.enabled = true ELSE ot.team_id = :teamId END)
                ORDER BY a.created_at ASC
            """, nativeQuery = true)
    List<OperatorAccount> findAllByTeam(String teamId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM operator_account_teams WHERE operator_id in (:ids)", nativeQuery = true)
    void deleteOpAcountTeamsByIdIn(List<String> ids);
}
