package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OperatorSurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorSurveyAnswerRepository extends JpaRepository<OperatorSurveyAnswer, Long> {

    List<OperatorSurveyAnswer> findByQuestionIdIn(List<Long> questionIds);

    @Modifying
    @Query(value = """
            DELETE FROM operator_survey_answers
            WHERE question_id IN (
            SELECT id FROM operator_survey_questions WHERE survey_id IN (:surveyIds)
            )""", nativeQuery = true)
    void deleteAnswersBySurveyIds(List<String> surveyIds);
}
