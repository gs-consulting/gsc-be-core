package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * # SurveyStatisticSearchDto 調査統計検索 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyStatisticSearchDto", description = "# SurveyStatisticSearchDto 調査統計検索 ")
public class SurveyStatisticSearchDto {

  private String questionId = null;

  private String answerId = null;

  private Integer pageNumber = null;

  private Integer pageSize = null;

  private String arrangedBy = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyStatisticSearchDto surveyStatisticSearchDto = (SurveyStatisticSearchDto) o;
    return Objects.equals(this.questionId, surveyStatisticSearchDto.questionId) &&
        Objects.equals(this.answerId, surveyStatisticSearchDto.answerId) &&
        Objects.equals(this.pageNumber, surveyStatisticSearchDto.pageNumber) &&
        Objects.equals(this.pageSize, surveyStatisticSearchDto.pageSize) &&
        Objects.equals(this.arrangedBy, surveyStatisticSearchDto.arrangedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, answerId, pageNumber, pageSize, arrangedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyStatisticSearchDto {\n");
    sb.append("    questionId: ").append(toIndentedString(questionId)).append("\n");
    sb.append("    answerId: ").append(toIndentedString(answerId)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    arrangedBy: ").append(toIndentedString(arrangedBy)).append("\n");
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
