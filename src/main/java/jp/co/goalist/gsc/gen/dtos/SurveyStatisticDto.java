package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyStatisticQuestionsDto;
import jp.co.goalist.gsc.gen.dtos.SurveyUnansweredItemsDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * アンケートの統計画面 
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "SurveyStatisticDto", description = "アンケートの統計画面 ")
public class SurveyStatisticDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @Valid
  private List<@Valid SurveyStatisticQuestionsDto> questions = new ArrayList<>();

  @Valid
  private List<@Valid SurveyUnansweredItemsDto> unansweredApplicants = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyStatisticDto surveyStatisticDto = (SurveyStatisticDto) o;
    return Objects.equals(this.name, surveyStatisticDto.name) &&
        Objects.equals(this.questions, surveyStatisticDto.questions) &&
        Objects.equals(this.unansweredApplicants, surveyStatisticDto.unansweredApplicants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, questions, unansweredApplicants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyStatisticDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    questions: ").append(toIndentedString(questions)).append("\n");
    sb.append("    unansweredApplicants: ").append(toIndentedString(unansweredApplicants)).append("\n");
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
