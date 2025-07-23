package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartItemGroupDto;
import jp.co.goalist.gsc.gen.dtos.MediaColorDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * ボリューム推移タイムライン（入社後）
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaBarChartAfterJoinDto", description = "ボリューム推移タイムライン（入社後）")
public class MediaBarChartAfterJoinDto {

  @Valid
  private List<@Valid MediaBarChartItemGroupDto> groups = new ArrayList<>();

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
    MediaBarChartAfterJoinDto mediaBarChartAfterJoinDto = (MediaBarChartAfterJoinDto) o;
    return Objects.equals(this.groups, mediaBarChartAfterJoinDto.groups) &&
        Objects.equals(this.colors, mediaBarChartAfterJoinDto.colors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groups, colors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaBarChartAfterJoinDto {\n");
    sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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
