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
 * 通話履歴 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "HistoryCallItemsDto", description = "通話履歴 ")
public class HistoryCallItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String contactDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String pic;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryCallItemsDto historyCallItemsDto = (HistoryCallItemsDto) o;
    return Objects.equals(this.id, historyCallItemsDto.id) &&
        Objects.equals(this.contactDateTime, historyCallItemsDto.contactDateTime) &&
        Objects.equals(this.pic, historyCallItemsDto.pic) &&
        Objects.equals(this.memo, historyCallItemsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contactDateTime, pic, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryCallItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    contactDateTime: ").append(toIndentedString(contactDateTime)).append("\n");
    sb.append("    pic: ").append(toIndentedString(pic)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
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
