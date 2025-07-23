package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartSectionDto;
import jp.co.goalist.gsc.gen.dtos.MediaColorDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 詳細グラフの項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaBarChartItemGroupDto", description = "詳細グラフの項目")
public class MediaBarChartItemGroupDto {

  @NotBlank(message = "この項目は必須です。")
  private String month;

  @Valid
  private List<@Valid MediaBarChartSectionDto> items = new ArrayList<>();

  @Valid
  private List<@Valid MediaColorDto> colors = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaBarChartItemGroupDto mediaBarChartItemGroupDto = (MediaBarChartItemGroupDto) o;
    return Objects.equals(this.month, mediaBarChartItemGroupDto.month) &&
        Objects.equals(this.items, mediaBarChartItemGroupDto.items) &&
        Objects.equals(this.colors, mediaBarChartItemGroupDto.colors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(month, items, colors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaBarChartItemGroupDto {\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    colors: ").append(toIndentedString(colors)).append("\n");
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
