package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorSurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorSurveyQuestionRepository extends JpaRepository<OperatorSurveyQuestion, Long> {

    @Query("SELECT s.id FROM operator_survey_questions s WHERE s.survey.id = :id")
    List<Long> findAllQuestionIdsBySurveyId(String id);

    @Modifying
    @Query(value = "DELETE FROM operator_survey_questions WHERE survey_id IN (:surveyIds)", nativeQuery = true)
    void deleteQuestionsBySurveyIds(List<String> surveyIds);
}
