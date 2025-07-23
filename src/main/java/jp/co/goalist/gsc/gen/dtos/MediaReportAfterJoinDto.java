package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartAfterJoinDto;
import jp.co.goalist.gsc.gen.dtos.MediaCostListAfterJoinDto;
import jp.co.goalist.gsc.gen.dtos.MediaPieChartAfterJoinDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 入社後のテーブルとグラフ
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaReportAfterJoinDto", description = "入社後のテーブルとグラフ")
public class MediaReportAfterJoinDto {

  private MediaCostListAfterJoinDto costTable;

  private MediaBarChartAfterJoinDto barChart;

  private MediaPieChartAfterJoinDto pieChart;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaReportAfterJoinDto mediaReportAfterJoinDto = (MediaReportAfterJoinDto) o;
    return Objects.equals(this.costTable, mediaReportAfterJoinDto.costTable) &&
        Objects.equals(this.barChart, mediaReportAfterJoinDto.barChart) &&
        Objects.equals(this.pieChart, mediaReportAfterJoinDto.pieChart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costTable, barChart, pieChart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaReportAfterJoinDto {\n");
    sb.append("    costTable: ").append(toIndentedString(costTable)).append("\n");
    sb.append("    barChart: ").append(toIndentedString(barChart)).append("\n");
    sb.append("    pieChart: ").append(toIndentedString(pieChart)).append("\n");
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
