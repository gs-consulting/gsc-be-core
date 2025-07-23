package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaColorDto;
import jp.co.goalist.gsc.gen.dtos.MediaPieChartSectionDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 入社後定着単価
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaPieChartAfterJoinDto", description = "入社後定着単価")
public class MediaPieChartAfterJoinDto {

  @Valid
  private List<@Valid MediaPieChartSectionDto> oneWeek = new ArrayList<>();

  @Valid
  private List<@Valid MediaPieChartSectionDto> oneMonth = new ArrayList<>();

  @Valid
  private List<@Valid MediaPieChartSectionDto> twoMonth = new ArrayList<>();

  @Valid
  private List<@Valid MediaPieChartSectionDto> threeMonth = new ArrayList<>();

  @Valid
  private List<@Valid MediaPieChartSectionDto> sixMonth = new ArrayList<>();

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
    MediaPieChartAfterJoinDto mediaPieChartAfterJoinDto = (MediaPieChartAfterJoinDto) o;
    return Objects.equals(this.oneWeek, mediaPieChartAfterJoinDto.oneWeek) &&
        Objects.equals(this.oneMonth, mediaPieChartAfterJoinDto.oneMonth) &&
        Objects.equals(this.twoMonth, mediaPieChartAfterJoinDto.twoMonth) &&
        Objects.equals(this.threeMonth, mediaPieChartAfterJoinDto.threeMonth) &&
        Objects.equals(this.sixMonth, mediaPieChartAfterJoinDto.sixMonth) &&
        Objects.equals(this.colors, mediaPieChartAfterJoinDto.colors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oneWeek, oneMonth, twoMonth, threeMonth, sixMonth, colors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaPieChartAfterJoinDto {\n");
    sb.append("    oneWeek: ").append(toIndentedString(oneWeek)).append("\n");
    sb.append("    oneMonth: ").append(toIndentedString(oneMonth)).append("\n");
    sb.append("    twoMonth: ").append(toIndentedString(twoMonth)).append("\n");
    sb.append("    threeMonth: ").append(toIndentedString(threeMonth)).append("\n");
    sb.append("    sixMonth: ").append(toIndentedString(sixMonth)).append("\n");
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
