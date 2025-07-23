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
 * 選択方式 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyAnswerUpsertDto", description = "選択方式 ")
public class SurveyAnswerUpsertDto {

  private String id = null;

  private String answerText = null;

  private Boolean isFixed;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyAnswerUpsertDto surveyAnswerUpsertDto = (SurveyAnswerUpsertDto) o;
    return Objects.equals(this.id, surveyAnswerUpsertDto.id) &&
        Objects.equals(this.answerText, surveyAnswerUpsertDto.answerText) &&
        Objects.equals(this.isFixed, surveyAnswerUpsertDto.isFixed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, answerText, isFixed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyAnswerUpsertDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    answerText: ").append(toIndentedString(answerText)).append("\n");
    sb.append("    isFixed: ").append(toIndentedString(isFixed)).append("\n");
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
