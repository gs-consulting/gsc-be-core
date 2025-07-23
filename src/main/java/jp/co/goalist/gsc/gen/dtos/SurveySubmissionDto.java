package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.AnswerSubmissionDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * SurveySubmissionDto
 */

@lombok.Getter
@lombok.Setter
public class SurveySubmissionDto {

  @NotBlank(message = "この項目は必須です。")
  private String token;

  @Valid
  private List<@Valid AnswerSubmissionDto> content = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveySubmissionDto surveySubmissionDto = (SurveySubmissionDto) o;
    return Objects.equals(this.token, surveySubmissionDto.token) &&
        Objects.equals(this.content, surveySubmissionDto.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveySubmissionDto {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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
