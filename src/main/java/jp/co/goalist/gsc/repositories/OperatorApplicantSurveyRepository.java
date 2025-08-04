package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.survey.ApplicantUnansweredItemDto;
import jp.co.goalist.gsc.entities.OperatorApplicantSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorApplicantSurveyRepository extends JpaRepository<OperatorApplicantSurvey, String> {

    Optional<OperatorApplicantSurvey> findBySurveyIdAndApplicantId(String surveyId, String applicantId);

    @Query(value = """
            SELECT a.id AS applicantId, a.full_name AS fullName, a.gender AS gender,
                TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE) AS age,
                CONCAT (pr.name, a.city) AS address, (a.is_mail_duplicate OR a.is_tel_duplicate) AS isDuplicate,
                CASE WHEN a.blacklist1_id IS NOT null OR a.blacklist2_id IS NOT null OR a.is_mail_duplicate OR a.is_tel_duplicate THEN false ELSE true END AS isValid,
                (a.blacklist1_id IS NOT NULL OR a.blacklist2_id IS NOT NULL) AS isBlacklist
            FROM operator_applicant_surveys s
            JOIN operator_applicants a ON a.id = s.applicant_id
            LEFT JOIN prefectures pr ON a.prefecture_id = pr.id
            WHERE s.survey_id = :surveyId AND s.replied_at IS NULL ORDER BY s.sent_at DESC
            """, nativeQuery = true)
    List<ApplicantUnansweredItemDto> findAllApplicantUnansweredBySurveyId(String surveyId);

    @Modifying
    @Query(value = "DELETE FROM operator_applicant_surveys WHERE applicant_id IN :applicantIds", nativeQuery = true)
    void deleteByApplicantIdIn(List<String> applicantIds);
}
