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
 * テンプレート管理 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SurveyUnansweredItemsDto", description = "テンプレート管理 ")
public class SurveyUnansweredItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String gender = null;

  private Integer age = null;

  private String address = null;

  private Boolean isDuplicate;

  private Boolean isValid;

  private Boolean isBlacklist;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyUnansweredItemsDto surveyUnansweredItemsDto = (SurveyUnansweredItemsDto) o;
    return Objects.equals(this.id, surveyUnansweredItemsDto.id) &&
        Objects.equals(this.fullName, surveyUnansweredItemsDto.fullName) &&
        Objects.equals(this.gender, surveyUnansweredItemsDto.gender) &&
        Objects.equals(this.age, surveyUnansweredItemsDto.age) &&
        Objects.equals(this.address, surveyUnansweredItemsDto.address) &&
        Objects.equals(this.isDuplicate, surveyUnansweredItemsDto.isDuplicate) &&
        Objects.equals(this.isValid, surveyUnansweredItemsDto.isValid) &&
        Objects.equals(this.isBlacklist, surveyUnansweredItemsDto.isBlacklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, gender, age, address, isDuplicate, isValid, isBlacklist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyUnansweredItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    isDuplicate: ").append(toIndentedString(isDuplicate)).append("\n");
    sb.append("    isValid: ").append(toIndentedString(isValid)).append("\n");
    sb.append("    isBlacklist: ").append(toIndentedString(isBlacklist)).append("\n");
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
