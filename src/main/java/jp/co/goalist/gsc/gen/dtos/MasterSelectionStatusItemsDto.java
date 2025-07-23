package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 選考ステータス一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MasterSelectionStatusItemsDto", description = "選考ステータス一覧 ")
public class MasterSelectionStatusItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Integer flowOrder;

  @NotNull(message = "この項目は必須です。")
  private Integer displayOrder;

  private Boolean isDeletable;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MasterSelectionStatusItemsDto masterSelectionStatusItemsDto = (MasterSelectionStatusItemsDto) o;
    return Objects.equals(this.id, masterSelectionStatusItemsDto.id) &&
        Objects.equals(this.name, masterSelectionStatusItemsDto.name) &&
        Objects.equals(this.flowOrder, masterSelectionStatusItemsDto.flowOrder) &&
        Objects.equals(this.displayOrder, masterSelectionStatusItemsDto.displayOrder) &&
        Objects.equals(this.isDeletable, masterSelectionStatusItemsDto.isDeletable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, flowOrder, displayOrder, isDeletable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterSelectionStatusItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    flowOrder: ").append(toIndentedString(flowOrder)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
    sb.append("    isDeletable: ").append(toIndentedString(isDeletable)).append("\n");
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
