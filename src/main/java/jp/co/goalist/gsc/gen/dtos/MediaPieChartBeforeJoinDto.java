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
 * プロセス別経路割合
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaPieChartBeforeJoinDto", description = "プロセス別経路割合")
public class MediaPieChartBeforeJoinDto {

  @Valid
  private List<@Valid MediaPieChartSectionDto> total = new ArrayList<>();

  @Valid
  private List<@Valid MediaPieChartSectionDto> apply;

  @Valid
  private List<@Valid MediaPieChartSectionDto> interview;

  @Valid
  private List<@Valid MediaPieChartSectionDto> offer;

  @Valid
  private List<@Valid MediaPieChartSectionDto> agreement;

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
    MediaPieChartBeforeJoinDto mediaPieChartBeforeJoinDto = (MediaPieChartBeforeJoinDto) o;
    return Objects.equals(this.total, mediaPieChartBeforeJoinDto.total) &&
        Objects.equals(this.apply, mediaPieChartBeforeJoinDto.apply) &&
        Objects.equals(this.interview, mediaPieChartBeforeJoinDto.interview) &&
        Objects.equals(this.offer, mediaPieChartBeforeJoinDto.offer) &&
        Objects.equals(this.agreement, mediaPieChartBeforeJoinDto.agreement) &&
        Objects.equals(this.colors, mediaPieChartBeforeJoinDto.colors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, apply, interview, offer, agreement, colors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaPieChartBeforeJoinDto {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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
