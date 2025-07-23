package jp.co.goalist.gsc.dtos.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerSummaryItemDto {

    private String applicantId;

    private Long questionId;

    private Long answerId;

    private String freeText;
}
