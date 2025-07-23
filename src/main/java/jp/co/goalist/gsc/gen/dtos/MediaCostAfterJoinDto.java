package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.MediaCostColumnDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 最初の表の各行の項目
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaCostAfterJoinDto", description = "最初の表の各行の項目")
public class MediaCostAfterJoinDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Integer amount;

  private MediaCostColumnDto oneWeek = null;

  private MediaCostColumnDto oneMonth = null;

  private MediaCostColumnDto twoMonth = null;

  private MediaCostColumnDto threeMonth = null;

  private MediaCostColumnDto sixMonth = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaCostAfterJoinDto mediaCostAfterJoinDto = (MediaCostAfterJoinDto) o;
    return Objects.equals(this.name, mediaCostAfterJoinDto.name) &&
        Objects.equals(this.amount, mediaCostAfterJoinDto.amount) &&
        Objects.equals(this.oneWeek, mediaCostAfterJoinDto.oneWeek) &&
        Objects.equals(this.oneMonth, mediaCostAfterJoinDto.oneMonth) &&
        Objects.equals(this.twoMonth, mediaCostAfterJoinDto.twoMonth) &&
        Objects.equals(this.threeMonth, mediaCostAfterJoinDto.threeMonth) &&
        Objects.equals(this.sixMonth, mediaCostAfterJoinDto.sixMonth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, amount, oneWeek, oneMonth, twoMonth, threeMonth, sixMonth);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaCostAfterJoinDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    oneWeek: ").append(toIndentedString(oneWeek)).append("\n");
    sb.append("    oneMonth: ").append(toIndentedString(oneMonth)).append("\n");
    sb.append("    twoMonth: ").append(toIndentedString(twoMonth)).append("\n");
    sb.append("    threeMonth: ").append(toIndentedString(threeMonth)).append("\n");
    sb.append("    sixMonth: ").append(toIndentedString(sixMonth)).append("\n");
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
