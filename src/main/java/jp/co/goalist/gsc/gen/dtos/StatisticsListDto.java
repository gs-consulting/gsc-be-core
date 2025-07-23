package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.StatisticsRecentDto;
import jp.co.goalist.gsc.gen.dtos.StatisticsTodayDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 社内ユーザー情報 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StatisticsListDto", description = "社内ユーザー情報 ")
public class StatisticsListDto {

  private StatisticsTodayDto today;

  private StatisticsRecentDto recent;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsListDto statisticsListDto = (StatisticsListDto) o;
    return Objects.equals(this.today, statisticsListDto.today) &&
        Objects.equals(this.recent, statisticsListDto.recent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(today, recent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsListDto {\n");
    sb.append("    today: ").append(toIndentedString(today)).append("\n");
    sb.append("    recent: ").append(toIndentedString(recent)).append("\n");
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
