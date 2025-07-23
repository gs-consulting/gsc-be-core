package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OperatorHistoryCall;

@Repository
public interface OperatorHistoryCallRepository extends JpaRepository<OperatorHistoryCall, String> {

    @Query("""
            from operator_history_calls c
            join fetch operator_client_accounts oa on c.pic.id = oa.id
            where c.applicant.id = :applicantId and c.applicant.parent.id = :parentId
            """)
    List<OperatorHistoryCall> findByApplicantId(String applicantId, String parentId);

    @Query("""
            from operator_history_calls
            where id in (:ids) and applicant.id = :applicantId and applicant.parent.id = :parentId
            """)
    List<OperatorHistoryCall> findAllByIdsAndApplicantId(List<String> ids, String applicantId, String parentId);

    @Query("""
            from operator_history_calls c
            join fetch operator_client_accounts oa on c.pic.id = oa.id
            where c.id = :historyId and c.applicant.id = :applicantId and c.applicant.parent.id = :parentId
            """)
    Optional<OperatorHistoryCall> findOneBy(String historyId, String applicantId, String parentId);
}
