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
 * 面接スケジュール
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "DInterviewItemsDto", description = "面接スケジュール")
public class DInterviewItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String categoryName;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  private String interviewDate;

  private String mediaName = null;

  private String projectName = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DInterviewItemsDto dinterviewItemsDto = (DInterviewItemsDto) o;
    return Objects.equals(this.id, dinterviewItemsDto.id) &&
        Objects.equals(this.categoryName, dinterviewItemsDto.categoryName) &&
        Objects.equals(this.fullName, dinterviewItemsDto.fullName) &&
        Objects.equals(this.interviewDate, dinterviewItemsDto.interviewDate) &&
        Objects.equals(this.mediaName, dinterviewItemsDto.mediaName) &&
        Objects.equals(this.projectName, dinterviewItemsDto.projectName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, categoryName, fullName, interviewDate, mediaName, projectName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DInterviewItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    categoryName: ").append(toIndentedString(categoryName)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    interviewDate: ").append(toIndentedString(interviewDate)).append("\n");
    sb.append("    mediaName: ").append(toIndentedString(mediaName)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
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
