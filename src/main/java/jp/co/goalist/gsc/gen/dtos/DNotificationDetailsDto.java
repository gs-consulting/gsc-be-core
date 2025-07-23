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
 * お知らせ詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "DNotificationDetailsDto", description = "お知らせ詳細 ")
public class DNotificationDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String title;

  @NotBlank(message = "この項目は必須です。")
  private String content;

  @NotBlank(message = "この項目は必須です。")
  private String postingStartDate;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DNotificationDetailsDto dnotificationDetailsDto = (DNotificationDetailsDto) o;
    return Objects.equals(this.id, dnotificationDetailsDto.id) &&
        Objects.equals(this.title, dnotificationDetailsDto.title) &&
        Objects.equals(this.content, dnotificationDetailsDto.content) &&
        Objects.equals(this.postingStartDate, dnotificationDetailsDto.postingStartDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, postingStartDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DNotificationDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    postingStartDate: ").append(toIndentedString(postingStartDate)).append("\n");
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
