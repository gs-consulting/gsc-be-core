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
@Schema(name = "DNotificationItemsDto", description = "お知らせ設定一覧 ")
public class DNotificationItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String title;

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
    DNotificationItemsDto dnotificationItemsDto = (DNotificationItemsDto) o;
    return Objects.equals(this.id, dnotificationItemsDto.id) &&
        Objects.equals(this.title, dnotificationItemsDto.title) &&
        Objects.equals(this.postingStartDate, dnotificationItemsDto.postingStartDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, postingStartDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DNotificationItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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
