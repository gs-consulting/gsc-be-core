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
 * 最初の表の各列の項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaCostColumnDto", description = "最初の表の各列の項目")
public class MediaCostColumnDto {

  @NotNull(message = "この項目は必須です。")
  private Long personCount;

  @NotNull(message = "この項目は必須です。")
  private Integer cost;

  private Double ratio = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaCostColumnDto mediaCostColumnDto = (MediaCostColumnDto) o;
    return Objects.equals(this.personCount, mediaCostColumnDto.personCount) &&
        Objects.equals(this.cost, mediaCostColumnDto.cost) &&
        Objects.equals(this.ratio, mediaCostColumnDto.ratio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personCount, cost, ratio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaCostColumnDto {\n");
    sb.append("    personCount: ").append(toIndentedString(personCount)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
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
