package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyAnswersDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * テンプレート管理 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyQuestionsDto", description = "テンプレート管理 ")
public class SurveyQuestionsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String questionText;

  @NotBlank(message = "この項目は必須です。")
  private String type;

  private Boolean isRequired;

  @Valid
  private List<@Valid SurveyAnswersDto> answers;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyQuestionsDto surveyQuestionsDto = (SurveyQuestionsDto) o;
    return Objects.equals(this.id, surveyQuestionsDto.id) &&
        Objects.equals(this.questionText, surveyQuestionsDto.questionText) &&
        Objects.equals(this.type, surveyQuestionsDto.type) &&
        Objects.equals(this.isRequired, surveyQuestionsDto.isRequired) &&
        Objects.equals(this.answers, surveyQuestionsDto.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, questionText, type, isRequired, answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyQuestionsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    questionText: ").append(toIndentedString(questionText)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    isRequired: ").append(toIndentedString(isRequired)).append("\n");
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
