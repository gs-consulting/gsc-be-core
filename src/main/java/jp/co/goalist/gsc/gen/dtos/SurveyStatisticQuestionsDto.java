package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyStatisticAnswersDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 選択方式 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyStatisticQuestionsDto", description = "選択方式 ")
public class SurveyStatisticQuestionsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String questionType;

  @NotBlank(message = "この項目は必須です。")
  private String questionText;

  @Valid
  private List<@Valid SurveyStatisticAnswersDto> answers;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyStatisticQuestionsDto surveyStatisticQuestionsDto = (SurveyStatisticQuestionsDto) o;
    return Objects.equals(this.id, surveyStatisticQuestionsDto.id) &&
        Objects.equals(this.questionType, surveyStatisticQuestionsDto.questionType) &&
        Objects.equals(this.questionText, surveyStatisticQuestionsDto.questionText) &&
        Objects.equals(this.answers, surveyStatisticQuestionsDto.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, questionType, questionText, answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyStatisticQuestionsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    questionType: ").append(toIndentedString(questionType)).append("\n");
    sb.append("    questionText: ").append(toIndentedString(questionText)).append("\n");
    sb.append("    answers: ").append(toIndentedString(answers)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
