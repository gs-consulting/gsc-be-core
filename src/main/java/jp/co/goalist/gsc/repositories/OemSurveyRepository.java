package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OemSurveyRepository extends JpaRepository<OemSurvey, String> {

    @Query("""
        FROM oem_surveys o WHERE o.parent.id = :parentId AND o.oemGroupId = :oemGroupId
        AND (:searchInput IS NULL OR o.surveyName LIKE :searchInput)
    """)
    Page<OemSurvey> findAllByParentIdAndOemGroupId(String parentId, String oemGroupId, String searchInput, Pageable pageable);

    @Query("""
            FROM oem_surveys s
            LEFT JOIN FETCH oem_survey_questions q ON s.id = q.survey.id
            WHERE s.id = :id AND s.parent.id = :parentId AND s.oemGroupId = :oemGroupId
            """)
    Optional<OemSurvey> findFetchById(String id, String parentId, String oemGroupId);

    @Query("""
            FROM oem_surveys s
            LEFT JOIN FETCH oem_survey_questions q ON q.survey.id = s.id
            LEFT JOIN FETCH oem_survey_answers a ON a.question.id = q.id
            WHERE s.id = :id AND s.parent.id = :parentId AND s.oemGroupId = :oemGroupId ORDER BY a.question.id ASC
            """)
    Optional<OemSurvey> findFetchAnswersById(String id, String parentId, String oemGroupId);

    @Query("FROM oem_surveys s WHERE s.id in (:ids) AND s.parent.id = :parentId AND s.oemGroupId = :oemGroupId")
    List<OemSurvey> findInIds(List<String> ids, String parentId, String oemGroupId);

    @Modifying
    @Query(value = "DELETE FROM oem_surveys WHERE id IN (:surveyIds)", nativeQuery = true)
    void deleteSurveysByIds(List<String> surveyIds);

    @Query("""
            FROM oem_surveys s
            WHERE s.parent.id = :parentId AND s.oemGroupId = :oemGroupId AND (:searchInput IS NULL OR s.surveyName LIKE :searchInput)
            """)
    Page<OemSurvey> findAllBy(String parentId, String oemGroupId, String searchInput, Pageable pageable);

    @Query("""
            SELECT s
            FROM oem_surveys s
            WHERE s.parent.id = :parentId
              AND s.oemGroupId = :oemGroupId
              AND (:searchInput IS NULL OR s.surveyName LIKE :searchInput)
              AND NOT EXISTS (
                  SELECT 1 FROM oem_applicant_surveys a
                  WHERE a.surveyId = s.id
                    AND a.applicantId = :applicantId
                    AND a.repliedAt IS NOT NULL
              )
            """)
    Page<OemSurvey> findAllUnsubmittedByApplicantId(
            String parentId,
            String oemGroupId,
            String searchInput,
            String applicantId,
            Pageable pageable
    );
}