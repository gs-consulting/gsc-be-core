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
 * お知らせ設定一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "NotificationItemsDto", description = "お知らせ設定一覧 ")
public class NotificationItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String title;

  @NotBlank(message = "この項目は必須です。")
  private String content;

  @NotBlank(message = "この項目は必須です。")
  private String postingStartDate;

  @NotBlank(message = "この項目は必須です。")
  private String postingEndDate;

  @NotBlank(message = "この項目は必須です。")
  private String status;

  private Boolean isDeletable;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationItemsDto notificationItemsDto = (NotificationItemsDto) o;
    return Objects.equals(this.id, notificationItemsDto.id) &&
        Objects.equals(this.title, notificationItemsDto.title) &&
        Objects.equals(this.content, notificationItemsDto.content) &&
        Objects.equals(this.postingStartDate, notificationItemsDto.postingStartDate) &&
        Objects.equals(this.postingEndDate, notificationItemsDto.postingEndDate) &&
        Objects.equals(this.status, notificationItemsDto.status) &&
        Objects.equals(this.isDeletable, notificationItemsDto.isDeletable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, postingStartDate, postingEndDate, status, isDeletable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    postingStartDate: ").append(toIndentedString(postingStartDate)).append("\n");
    sb.append("    postingEndDate: ").append(toIndentedString(postingEndDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    isDeletable: ").append(toIndentedString(isDeletable)).append("\n");
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
