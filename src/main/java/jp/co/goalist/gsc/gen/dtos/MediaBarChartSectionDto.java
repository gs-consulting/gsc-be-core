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
 * バーグラフの項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaBarChartSectionDto", description = "バーグラフの項目")
public class MediaBarChartSectionDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Long personCount;

  @NotNull(message = "この項目は必須です。")
  private Integer amount;

  private Double ratio;

  private Double amountRatio;

  private String color = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaBarChartSectionDto mediaBarChartSectionDto = (MediaBarChartSectionDto) o;
    return Objects.equals(this.name, mediaBarChartSectionDto.name) &&
        Objects.equals(this.personCount, mediaBarChartSectionDto.personCount) &&
        Objects.equals(this.amount, mediaBarChartSectionDto.amount) &&
        Objects.equals(this.ratio, mediaBarChartSectionDto.ratio) &&
        Objects.equals(this.amountRatio, mediaBarChartSectionDto.amountRatio) &&
        Objects.equals(this.color, mediaBarChartSectionDto.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, personCount, amount, ratio, amountRatio, color);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaBarChartSectionDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    personCount: ").append(toIndentedString(personCount)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    ratio: ").append(toIndentedString(ratio)).append("\n");
    sb.append("    amountRatio: ").append(toIndentedString(amountRatio)).append("\n");
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
