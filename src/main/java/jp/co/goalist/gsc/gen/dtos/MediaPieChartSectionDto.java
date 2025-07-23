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
 * 円グラフの項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaPieChartSectionDto", description = "円グラフの項目")
public class MediaPieChartSectionDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Long personCount;

  @NotNull(message = "この項目は必須です。")
  private Integer cost;

  private Double ratio;

  private String color = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaPieChartSectionDto mediaPieChartSectionDto = (MediaPieChartSectionDto) o;
    return Objects.equals(this.name, mediaPieChartSectionDto.name) &&
        Objects.equals(this.personCount, mediaPieChartSectionDto.personCount) &&
        Objects.equals(this.cost, mediaPieChartSectionDto.cost) &&
        Objects.equals(this.ratio, mediaPieChartSectionDto.ratio) &&
        Objects.equals(this.color, mediaPieChartSectionDto.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, personCount, cost, ratio, color);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaPieChartSectionDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    personCount: ").append(toIndentedString(personCount)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
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
