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
 * お知らせ詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "NotificationDetailsDto", description = "お知らせ詳細 ")
public class NotificationDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String title;

  @NotBlank(message = "この項目は必須です。")
  private String content;

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate postingStartDate;

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate postingEndDate;

  @NotBlank(message = "この項目は必須です。")
  private String status;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDetailsDto notificationDetailsDto = (NotificationDetailsDto) o;
    return Objects.equals(this.id, notificationDetailsDto.id) &&
        Objects.equals(this.title, notificationDetailsDto.title) &&
        Objects.equals(this.content, notificationDetailsDto.content) &&
        Objects.equals(this.postingStartDate, notificationDetailsDto.postingStartDate) &&
        Objects.equals(this.postingEndDate, notificationDetailsDto.postingEndDate) &&
        Objects.equals(this.status, notificationDetailsDto.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, postingStartDate, postingEndDate, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    postingStartDate: ").append(toIndentedString(postingStartDate)).append("\n");
    sb.append("    postingEndDate: ").append(toIndentedString(postingEndDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
