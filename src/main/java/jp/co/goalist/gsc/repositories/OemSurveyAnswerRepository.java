package jp.co.goalist.gsc.repositories;

import jp.co.goalist.gsc.entities.OemSurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OemSurveyAnswerRepository extends JpaRepository<OemSurveyAnswer, Long> {

    List<OemSurveyAnswer> findByQuestionIdIn(List<Long> questionIds);

    @Modifying
    @Query(value = """
                    DELETE FROM oem_survey_answers
                    WHERE question_id IN (SELECT id FROM oem_survey_questions WHERE survey_id IN (:surveyIds))
                    """, nativeQuery = true)
    void deleteAnswersBySurveyIds(List<String> surveyIds);
}
