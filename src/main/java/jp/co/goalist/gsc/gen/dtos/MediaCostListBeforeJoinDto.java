package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MediaCostBeforeJoinDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 最初の表の項目一覧
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "MediaCostListBeforeJoinDto", description = "最初の表の項目一覧")
public class MediaCostListBeforeJoinDto {

  private MediaCostBeforeJoinDto total = null;

  @Valid
  private List<@Valid MediaCostBeforeJoinDto> items = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaCostListBeforeJoinDto mediaCostListBeforeJoinDto = (MediaCostListBeforeJoinDto) o;
    return Objects.equals(this.total, mediaCostListBeforeJoinDto.total) &&
        Objects.equals(this.items, mediaCostListBeforeJoinDto.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaCostListBeforeJoinDto {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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
