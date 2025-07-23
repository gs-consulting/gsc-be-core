package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.StatisticsTodayDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 最近の採用状況
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StatisticsRecentDto", description = "最近の採用状況")
public class StatisticsRecentDto {

  private StatisticsTodayDto days;

  private StatisticsTodayDto lastMonth;

  private StatisticsTodayDto twoMonths;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsRecentDto statisticsRecentDto = (StatisticsRecentDto) o;
    return Objects.equals(this.days, statisticsRecentDto.days) &&
        Objects.equals(this.lastMonth, statisticsRecentDto.lastMonth) &&
        Objects.equals(this.twoMonths, statisticsRecentDto.twoMonths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(days, lastMonth, twoMonths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsRecentDto {\n");
    sb.append("    days: ").append(toIndentedString(days)).append("\n");
    sb.append("    lastMonth: ").append(toIndentedString(lastMonth)).append("\n");
    sb.append("    twoMonths: ").append(toIndentedString(twoMonths)).append("\n");
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
