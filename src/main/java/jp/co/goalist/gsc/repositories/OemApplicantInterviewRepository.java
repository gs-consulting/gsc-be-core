package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemApplicantInterview;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OemApplicantInterviewRepository extends JpaRepository<OemApplicantInterview, String> {

    @Query(value = """
                SELECT oai
                FROM oem_applicant_interviews oai
                WHERE oai.picId = :picId
                AND oai.interviewStartDate >= CURRENT_TIMESTAMP
                ORDER BY oai.interviewStartDate ASC
            """)
    List<OemApplicantInterview> getDashboardInterviews(String picId);

    @Modifying
    @Query(value = """
                DELETE FROM oem_applicant_interviews
                WHERE applicant_id IN (:applicantIds)
                AND parent_id = :parentId
                AND oem_group_id = :oemGroupId
            """, nativeQuery = true)
    void deleteByApplicantIdIn(List<String> applicantIds, String parentId, String oemGroupId);
}
