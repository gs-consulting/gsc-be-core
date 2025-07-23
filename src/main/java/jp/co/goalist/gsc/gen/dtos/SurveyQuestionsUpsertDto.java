package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyAnswerUpsertDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * アンケート管理 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyQuestionsUpsertDto", description = "アンケート管理 ")
public class SurveyQuestionsUpsertDto {

  private String id = null;

  @NotBlank(message = "この項目は必須です。")
  private String questionText;

  @NotBlank(message = "この項目は必須です。")
  private String type;

  private Boolean isRequired;

  @Valid
  private List<@Valid SurveyAnswerUpsertDto> answers;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyQuestionsUpsertDto surveyQuestionsUpsertDto = (SurveyQuestionsUpsertDto) o;
    return Objects.equals(this.id, surveyQuestionsUpsertDto.id) &&
        Objects.equals(this.questionText, surveyQuestionsUpsertDto.questionText) &&
        Objects.equals(this.type, surveyQuestionsUpsertDto.type) &&
        Objects.equals(this.isRequired, surveyQuestionsUpsertDto.isRequired) &&
        Objects.equals(this.answers, surveyQuestionsUpsertDto.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, questionText, type, isRequired, answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyQuestionsUpsertDto {\n");
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
