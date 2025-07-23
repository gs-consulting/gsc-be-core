package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaBarChartBeforeJoinItemsDto;
import jp.co.goalist.gsc.gen.dtos.MediaColorDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * ボリューム推移タイムライン（入社前）
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaBarChartBeforeJoinDto", description = "ボリューム推移タイムライン（入社前）")
public class MediaBarChartBeforeJoinDto {

  private MediaBarChartBeforeJoinItemsDto apply = null;

  private MediaBarChartBeforeJoinItemsDto interview = null;

  private MediaBarChartBeforeJoinItemsDto offer = null;

  private MediaBarChartBeforeJoinItemsDto agreement = null;

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
    MediaBarChartBeforeJoinDto mediaBarChartBeforeJoinDto = (MediaBarChartBeforeJoinDto) o;
    return Objects.equals(this.apply, mediaBarChartBeforeJoinDto.apply) &&
        Objects.equals(this.interview, mediaBarChartBeforeJoinDto.interview) &&
        Objects.equals(this.offer, mediaBarChartBeforeJoinDto.offer) &&
        Objects.equals(this.agreement, mediaBarChartBeforeJoinDto.agreement) &&
        Objects.equals(this.colors, mediaBarChartBeforeJoinDto.colors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apply, interview, offer, agreement, colors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaBarChartBeforeJoinDto {\n");
    sb.append("    apply: ").append(toIndentedString(apply)).append("\n");
    sb.append("    interview: ").append(toIndentedString(interview)).append("\n");
    sb.append("    offer: ").append(toIndentedString(offer)).append("\n");
    sb.append("    agreement: ").append(toIndentedString(agreement)).append("\n");
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
