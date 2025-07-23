package jp.co.goalist.gsc.dtos.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyApplicantAnswerItemDto {

    private String applicantId;

    private String fullName;

    private String gender;

    private Long age;

    private Long questionId;

    private Long answerId;

    private String freeText;

    private String answerText;

    private Timestamp answerDateTime;
}
