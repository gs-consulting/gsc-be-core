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
 * 通話履歴登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "HistoryCallUpsertDto", description = "通話履歴登録 ")
public class HistoryCallUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String contactStartDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String contactEndDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String picId;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryCallUpsertDto historyCallUpsertDto = (HistoryCallUpsertDto) o;
    return Objects.equals(this.contactStartDateTime, historyCallUpsertDto.contactStartDateTime) &&
        Objects.equals(this.contactEndDateTime, historyCallUpsertDto.contactEndDateTime) &&
        Objects.equals(this.picId, historyCallUpsertDto.picId) &&
        Objects.equals(this.memo, historyCallUpsertDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactStartDateTime, contactEndDateTime, picId, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryCallUpsertDto {\n");
    sb.append("    contactStartDateTime: ").append(toIndentedString(contactStartDateTime)).append("\n");
    sb.append("    contactEndDateTime: ").append(toIndentedString(contactEndDateTime)).append("\n");
    sb.append("    picId: ").append(toIndentedString(picId)).append("\n");
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
