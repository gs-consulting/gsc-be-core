package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.OemAccountItemsDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * OEMアカウント一覧 
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "OemAccountListDto", description = "OEMアカウント一覧 ")
public class OemAccountListDto {

  @NotNull(message = "この項目は必須です。")
  private Integer page = 1;

  @NotNull(message = "この項目は必須です。")
  private Integer limit = 10;

  @NotNull(message = "この項目は必須です。")
  private Long total = 0l;

  @Valid
  private List<@Valid OemAccountItemsDto> items = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OemAccountListDto oemAccountListDto = (OemAccountListDto) o;
    return Objects.equals(this.page, oemAccountListDto.page) &&
        Objects.equals(this.limit, oemAccountListDto.limit) &&
        Objects.equals(this.total, oemAccountListDto.total) &&
        Objects.equals(this.items, oemAccountListDto.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, limit, total, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OemAccountListDto {\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
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
