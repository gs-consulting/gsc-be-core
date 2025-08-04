package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.dtos.survey.SurveyAnswerSummaryItemDto;
import jp.co.goalist.gsc.dtos.survey.SurveyApplicantAnswerItemDto;
import jp.co.goalist.gsc.entities.OemApplicantAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemApplicantAnswerRepository extends JpaRepository<OemApplicantAnswer, Long> {

    @Query(value = """
            SELECT s.applicant_id AS applicantId,
                oaa.question_id AS questionId,
                oaa.selected_answer_id AS answerId,
                oaa.answer_text AS freeText
            FROM oem_applicant_answers oaa
            JOIN oem_applicant_surveys s ON oaa.applicant_survey_id = s.id
            WHERE s.survey_id = :surveyId
            """, nativeQuery = true)
    List<SurveyAnswerSummaryItemDto> findAllAnswerSummaryBySurveyId(String surveyId);

    @Query("""
            SELECT DISTINCT a.id
            FROM oem_applicant_answers oaa
            JOIN oem_applicant_surveys s ON s.id = oaa.applicantSurveyId
            JOIN oem_applicants a ON a.id = s.applicantId
            WHERE s.surveyId = :surveyId
            """)
    Page<String> findApplicantIdsBySurveyId(String surveyId, Pageable pageable);

    @Query(value = """
            SELECT a.id AS applicantId, a.full_name AS fullName, a.gender AS gender,
                TIMESTAMPDIFF(YEAR, a.birthday, CURRENT_DATE) AS age,
                oaa.question_id AS questionId,
                oaa.selected_answer_id AS answerId,
                oaa.answer_text AS freeText,
                sa.answer_text AS answerText,
                s.sent_at AS answerDateTime
            FROM oem_applicant_answers oaa
            JOIN oem_applicant_surveys s ON s.id = oaa.applicant_survey_id
            LEFT JOIN oem_survey_answers sa ON sa.id = oaa.selected_answer_id
            JOIN oem_applicants a ON a.id = s.applicant_id
            WHERE s.survey_id = :surveyId AND a.id IN :applicantIds
            """, nativeQuery = true)
    Page<SurveyApplicantAnswerItemDto> findAllBySurveyIdAndApplicantIds(String surveyId, List<String> applicantIds, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM oem_applicant_answers WHERE applicant_survey_id IN (SELECT id FROM oem_applicant_surveys WHERE applicant_id IN :applicantIds)", nativeQuery = true)
    void deleteByApplicantIdIn(List<String> applicantIds);
}
