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
 * 媒体の表示色
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaColorDto", description = "媒体の表示色")
public class MediaColorDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String color;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaColorDto mediaColorDto = (MediaColorDto) o;
    return Objects.equals(this.name, mediaColorDto.name) &&
        Objects.equals(this.color, mediaColorDto.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, color);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaColorDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
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
