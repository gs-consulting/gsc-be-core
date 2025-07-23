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
 * SurveyLinkRequestDto
 */

@lombok.Getter
@lombok.Setter
public class SurveyLinkRequestDto {

  @NotBlank(message = "この項目は必須です。")
  private String surveyId;

  @NotBlank(message = "この項目は必須です。")
  private String applicantId;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyLinkRequestDto surveyLinkRequestDto = (SurveyLinkRequestDto) o;
    return Objects.equals(this.surveyId, surveyLinkRequestDto.surveyId) &&
        Objects.equals(this.applicantId, surveyLinkRequestDto.applicantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveyId, applicantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyLinkRequestDto {\n");
    sb.append("    surveyId: ").append(toIndentedString(surveyId)).append("\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
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
