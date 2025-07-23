package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyQuestionsUpsertDto;
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
@Schema(name = "SurveyUpsertDto", description = "テンプレート管理 ")
public class SurveyUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @Valid
  private List<@Valid SurveyQuestionsUpsertDto> content = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyUpsertDto surveyUpsertDto = (SurveyUpsertDto) o;
    return Objects.equals(this.name, surveyUpsertDto.name) &&
        Objects.equals(this.content, surveyUpsertDto.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyUpsertDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
