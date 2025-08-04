package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorApplicantInterview;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorApplicantInterviewRepository extends JpaRepository<OperatorApplicantInterview, String> {

    @Query(value = """
            SELECT opeai
            FROM operator_applicant_interviews opeai
            WHERE opeai.picId = :picId
            ORDER BY opeai.interviewStartDate DESC
            """)
    List<OperatorApplicantInterview> getDashboardInterviews(String picId);

    @Modifying
    @Query(value = """
                DELETE FROM operator_applicant_interviews
                WHERE applicant_id IN (:applicantIds) and parent_id = :parentId
            """, nativeQuery = true)
    void deleteByApplicantIdIn(List<String> applicantIds, String parentId);
}
