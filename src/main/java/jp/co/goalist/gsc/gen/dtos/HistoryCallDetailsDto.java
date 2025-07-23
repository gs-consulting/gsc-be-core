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
 * HistoryCallDetailsDto
 */

@lombok.Getter
@lombok.Setter
public class HistoryCallDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

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
    HistoryCallDetailsDto historyCallDetailsDto = (HistoryCallDetailsDto) o;
    return Objects.equals(this.id, historyCallDetailsDto.id) &&
        Objects.equals(this.contactStartDateTime, historyCallDetailsDto.contactStartDateTime) &&
        Objects.equals(this.contactEndDateTime, historyCallDetailsDto.contactEndDateTime) &&
        Objects.equals(this.picId, historyCallDetailsDto.picId) &&
        Objects.equals(this.memo, historyCallDetailsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contactStartDateTime, contactEndDateTime, picId, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryCallDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
