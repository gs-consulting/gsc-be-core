package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 案件一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectAdvertItemsDto", description = "案件一覧 ")
public class ProjectAdvertItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate publishingStartDate;

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate publishingEndDate;

  @NotBlank(message = "この項目は必須です。")
  private String advertisementName;

  @NotBlank(message = "この項目は必須です。")
  private String mediaName;

  private Integer amount = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectAdvertItemsDto projectAdvertItemsDto = (ProjectAdvertItemsDto) o;
    return Objects.equals(this.id, projectAdvertItemsDto.id) &&
        Objects.equals(this.publishingStartDate, projectAdvertItemsDto.publishingStartDate) &&
        Objects.equals(this.publishingEndDate, projectAdvertItemsDto.publishingEndDate) &&
        Objects.equals(this.advertisementName, projectAdvertItemsDto.advertisementName) &&
        Objects.equals(this.mediaName, projectAdvertItemsDto.mediaName) &&
        Objects.equals(this.amount, projectAdvertItemsDto.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, publishingStartDate, publishingEndDate, advertisementName, mediaName, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectAdvertItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    publishingStartDate: ").append(toIndentedString(publishingStartDate)).append("\n");
    sb.append("    publishingEndDate: ").append(toIndentedString(publishingEndDate)).append("\n");
    sb.append("    advertisementName: ").append(toIndentedString(advertisementName)).append("\n");
    sb.append("    mediaName: ").append(toIndentedString(mediaName)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
