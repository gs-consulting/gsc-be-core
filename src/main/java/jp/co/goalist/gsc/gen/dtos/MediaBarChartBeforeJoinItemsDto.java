package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartItemGroupDto;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartTotalDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * バーグラフで入社前の項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaBarChartBeforeJoinItemsDto", description = "バーグラフで入社前の項目")
public class MediaBarChartBeforeJoinItemsDto {

  private MediaBarChartTotalDto total;

  @Valid
  private List<@Valid MediaBarChartItemGroupDto> groups = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaBarChartBeforeJoinItemsDto mediaBarChartBeforeJoinItemsDto = (MediaBarChartBeforeJoinItemsDto) o;
    return Objects.equals(this.total, mediaBarChartBeforeJoinItemsDto.total) &&
        Objects.equals(this.groups, mediaBarChartBeforeJoinItemsDto.groups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, groups);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaBarChartBeforeJoinItemsDto {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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
