package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyQuestionsDto;
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
@Schema(name = "SurveyDetailsDto", description = "テンプレート管理 ")
public class SurveyDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  private Boolean isEditable;

  @Valid
  private List<@Valid SurveyQuestionsDto> content = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyDetailsDto surveyDetailsDto = (SurveyDetailsDto) o;
    return Objects.equals(this.id, surveyDetailsDto.id) &&
        Objects.equals(this.name, surveyDetailsDto.name) &&
        Objects.equals(this.isEditable, surveyDetailsDto.isEditable) &&
        Objects.equals(this.content, surveyDetailsDto.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, isEditable, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isEditable: ").append(toIndentedString(isEditable)).append("\n");
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
