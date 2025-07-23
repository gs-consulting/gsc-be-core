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
 * MasterMonthlyCostUpsertDto
 */

@lombok.Getter
@lombok.Setter
public class MasterMonthlyCostUpsertDto {

  private String id = null;

  @NotBlank(message = "この項目は必須です。")
  private String mediaId;

  @NotNull(message = "この項目は必須です。")
  private Integer amount;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MasterMonthlyCostUpsertDto masterMonthlyCostUpsertDto = (MasterMonthlyCostUpsertDto) o;
    return Objects.equals(this.id, masterMonthlyCostUpsertDto.id) &&
        Objects.equals(this.mediaId, masterMonthlyCostUpsertDto.mediaId) &&
        Objects.equals(this.amount, masterMonthlyCostUpsertDto.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, mediaId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterMonthlyCostUpsertDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
