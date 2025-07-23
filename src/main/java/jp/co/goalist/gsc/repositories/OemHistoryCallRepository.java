package jp.co.goalist.gsc.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.goalist.gsc.entities.OemHistoryCall;

@Repository
public interface OemHistoryCallRepository extends JpaRepository<OemHistoryCall, String> {

    @Query("""
            from oem_history_calls
            where applicant.id = :applicantId and applicant.parent.id = :parentId and applicant.oemGroupId = :oemGroupId
            """)
    List<OemHistoryCall> findByApplicantId(String applicantId, String parentId, String oemGroupId);

    @Query("""
            from oem_history_calls
            where id in (:ids) and applicant.id = :applicantId and applicant.parent.id = :parentId and applicant.oemGroupId = :oemGroupId
            """)
    List<OemHistoryCall> findAllByIdsAndApplicantId(List<String> ids, String applicantId, String parentId, String oemGroupId);

    @Query("""
            from oem_history_calls c
            join fetch oem_client_accounts oa on c.pic.id = oa.id
            where c.id = :historyId and c.applicant.id = :applicantId and c.applicant.parent.id = :parentId and applicant.oemGroupId = :oemGroupId
            """)
    Optional<OemHistoryCall> findOneBy(String historyId, String applicantId, String parentId, String oemGroupId);
}
