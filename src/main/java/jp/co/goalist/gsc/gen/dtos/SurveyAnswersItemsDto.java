package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@Schema(name = "SurveyAnswersItemsDto", description = "テンプレート管理 ")
public class SurveyAnswersItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime answerDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String gender = null;

  private Integer age = null;

  
  private List<String> answers;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyAnswersItemsDto surveyAnswersItemsDto = (SurveyAnswersItemsDto) o;
    return Objects.equals(this.id, surveyAnswersItemsDto.id) &&
        Objects.equals(this.answerDateTime, surveyAnswersItemsDto.answerDateTime) &&
        Objects.equals(this.fullName, surveyAnswersItemsDto.fullName) &&
        Objects.equals(this.gender, surveyAnswersItemsDto.gender) &&
        Objects.equals(this.age, surveyAnswersItemsDto.age) &&
        Objects.equals(this.answers, surveyAnswersItemsDto.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, answerDateTime, fullName, gender, age, answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyAnswersItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    answerDateTime: ").append(toIndentedString(answerDateTime)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
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
