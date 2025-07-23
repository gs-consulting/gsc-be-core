package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartBeforeJoinDto;
import jp.co.goalist.gsc.gen.dtos.MediaCostListBeforeJoinDto;
import jp.co.goalist.gsc.gen.dtos.MediaPieChartBeforeJoinDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 入社前のテーブルとグラフ
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaReportBeforeJoinDto", description = "入社前のテーブルとグラフ")
public class MediaReportBeforeJoinDto {

  private MediaCostListBeforeJoinDto costTable;

  private MediaBarChartBeforeJoinDto barChart;

  private MediaPieChartBeforeJoinDto pieChart;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaReportBeforeJoinDto mediaReportBeforeJoinDto = (MediaReportBeforeJoinDto) o;
    return Objects.equals(this.costTable, mediaReportBeforeJoinDto.costTable) &&
        Objects.equals(this.barChart, mediaReportBeforeJoinDto.barChart) &&
        Objects.equals(this.pieChart, mediaReportBeforeJoinDto.pieChart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costTable, barChart, pieChart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaReportBeforeJoinDto {\n");
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
