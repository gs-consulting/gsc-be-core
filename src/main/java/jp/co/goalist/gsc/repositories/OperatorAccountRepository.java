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
                select o.* from operator_accounts o 
                join accounts a on o.id = a.id 
                left join operator_account_teams ot on o.id = ot.operator_id
                where (case when :teamId is null then a.enabled = true else ot.team_id = :teamId end)
                order by a.created_at asc
            """, nativeQuery = true)
    Page<OperatorAccount> findAllEnabledManagersDropdown(Pageable pageable, String teamId);

    @Query(value = """
                select oc.* from operator_accounts oc where oc.id in 
                (select o.id
                    from operator_accounts o
                    join accounts a on o.id = a.id
                    left join operator_account_teams ot on a.id = ot.operator_id
                    left join operator_teams t on ot.team_id = t.id
                    where (:searchInput is null or o.full_name like :searchInput
                    or a.email like :searchInput or t.name like :searchInput))
                order by oc.created_at asc
            """,
            countQuery = """
                   select count(oc.*) from operator_accounts oc where oc.id in (select o.id
                    from operator_accounts o
                    join accounts a on o.id = a.id
                    left join operator_account_teams ot on a.id = ot.operator_id
                    left join operator_teams t on ot.team_id = t.id
                    where (:searchInput is null or o.full_name like :searchInput
                    or a.email like :searchInput or t.name like :searchInput))
                    order by oc.created_at asc
                    """, nativeQuery = true)
    Page<OperatorAccount> findAllBy(String searchInput, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from operator_account_teams where team_id in (:ids)", nativeQuery = true)
    void deleteOpAcountTeamsByIdIn(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from operator_accounts where id in (:ids)", nativeQuery = true)
    void deleteSelectedOperatorStaffsByIdIn(List<String> ids);
    
    @Query(value = """
                select o.* from operator_accounts o 
                join accounts a on o.id = a.id 
                left join operator_account_teams ot on o.id = ot.operator_id
                where (case when :teamId is null then a.enabled = true else ot.team_id = :teamId end)
                order by a.created_at asc
            """, nativeQuery = true)
    List<OperatorAccount> findAllByTeam(String teamId);
}
