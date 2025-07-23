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
@Schema(name = "SurveyAnswersDto", description = "選択方式 ")
public class SurveyAnswersDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String answerText;

  private Boolean isFixed;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyAnswersDto surveyAnswersDto = (SurveyAnswersDto) o;
    return Objects.equals(this.id, surveyAnswersDto.id) &&
        Objects.equals(this.answerText, surveyAnswersDto.answerText) &&
        Objects.equals(this.isFixed, surveyAnswersDto.isFixed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, answerText, isFixed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyAnswersDto {\n");
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
