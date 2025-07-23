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
@Schema(name = "MediaCostBeforeJoinDto", description = "最初の表の各行の項目")
public class MediaCostBeforeJoinDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Integer amount;

  private MediaCostColumnDto apply = null;

  private MediaCostColumnDto interview = null;

  private MediaCostColumnDto offer = null;

  private MediaCostColumnDto agreement = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaCostBeforeJoinDto mediaCostBeforeJoinDto = (MediaCostBeforeJoinDto) o;
    return Objects.equals(this.name, mediaCostBeforeJoinDto.name) &&
        Objects.equals(this.amount, mediaCostBeforeJoinDto.amount) &&
        Objects.equals(this.apply, mediaCostBeforeJoinDto.apply) &&
        Objects.equals(this.interview, mediaCostBeforeJoinDto.interview) &&
        Objects.equals(this.offer, mediaCostBeforeJoinDto.offer) &&
        Objects.equals(this.agreement, mediaCostBeforeJoinDto.agreement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, amount, apply, interview, offer, agreement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaCostBeforeJoinDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    apply: ").append(toIndentedString(apply)).append("\n");
    sb.append("    interview: ").append(toIndentedString(interview)).append("\n");
    sb.append("    offer: ").append(toIndentedString(offer)).append("\n");
    sb.append("    agreement: ").append(toIndentedString(agreement)).append("\n");
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
