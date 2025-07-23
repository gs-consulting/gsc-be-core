package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * AnswerSubmissionDto
 */

@lombok.Getter
@lombok.Setter
public class AnswerSubmissionDto {

  @NotBlank(message = "この項目は必須です。")
  private String questionId;

  
  private List<String> answerIds;

  private String answerText = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnswerSubmissionDto answerSubmissionDto = (AnswerSubmissionDto) o;
    return Objects.equals(this.questionId, answerSubmissionDto.questionId) &&
        Objects.equals(this.answerIds, answerSubmissionDto.answerIds) &&
        Objects.equals(this.answerText, answerSubmissionDto.answerText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, answerIds, answerText);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnswerSubmissionDto {\n");
    sb.append("    questionId: ").append(toIndentedString(questionId)).append("\n");
    sb.append("    answerIds: ").append(toIndentedString(answerIds)).append("\n");
    sb.append("    answerText: ").append(toIndentedString(answerText)).append("\n");
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
