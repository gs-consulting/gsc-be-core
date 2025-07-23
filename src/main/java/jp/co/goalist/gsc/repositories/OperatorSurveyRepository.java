package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorSurveyRepository extends JpaRepository<OperatorSurvey, String> {

    @Query("""
        FROM operator_surveys o WHERE o.parent.id = :parentId
        AND (:searchInput IS NULL OR o.surveyName LIKE :searchInput)
    """)
    Page<OperatorSurvey> findAllByParentId(String parentId, String searchInput, Pageable pageable);

    @Query("""
            FROM operator_surveys s
            LEFT JOIN FETCH operator_survey_questions q ON s.id = q.survey.id
            WHERE s.id = :id AND s.parent.id = :parentId
            """)
    Optional<OperatorSurvey> findFetchById(String id, String parentId);

    @Query("""
            FROM operator_surveys s
            LEFT JOIN FETCH operator_survey_questions q ON q.survey.id = s.id
            LEFT JOIN FETCH operator_survey_answers a ON a.question.id = q.id
            WHERE s.id = :id AND s.parent.id = :parentId ORDER BY a.question.id ASC
            """)
    Optional<OperatorSurvey> findFetchAnswersById(String id, String parentId);

    @Query("FROM operator_surveys s WHERE s.id IN (:ids) AND s.parent.id = :parentId")
    List<OperatorSurvey> findInIds(List<String> ids, String parentId);

    @Modifying
    @Query(value = "DELETE FROM operator_surveys WHERE id IN (:surveyIds)", nativeQuery = true)
    void deleteSurveysByIds(List<String> surveyIds);

    @Query("""
            FROM operator_surveys s
            WHERE s.parent.id = :parentId AND (:searchInput IS NULL OR s.surveyName LIKE :searchInput)
            """)
    Page<OperatorSurvey> findAllBy(String parentId, String searchInput, Pageable pageable);

    @Query("""
            SELECT s
            FROM operator_surveys s
            WHERE s.parent.id = :parentId
              AND (:searchInput IS NULL OR s.surveyName LIKE :searchInput)
              AND NOT EXISTS (
                  SELECT 1 FROM operator_applicant_surveys a
                  WHERE a.surveyId = s.id
                    AND a.applicantId = :applicantId
                    AND a.repliedAt IS NOT NULL
              )
            """)
    Page<OperatorSurvey> findAllUnsubmittedByApplicantId(
            String parentId,
            String searchInput,
            String applicantId,
            Pageable pageable
    );

}
