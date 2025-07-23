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
 * 媒体マスタ登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MasterMediaUpsertDto", description = "媒体マスタ登録 ")
public class MasterMediaUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  private String mediaCode = null;

  @NotNull(message = "この項目は必須です。")
  private Integer amount;

  private String hexColor = null;

  private String memo = null;

  private String siteName = null;

  private String managementLoginId = null;

  private String managementPwd = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MasterMediaUpsertDto masterMediaUpsertDto = (MasterMediaUpsertDto) o;
    return Objects.equals(this.name, masterMediaUpsertDto.name) &&
        Objects.equals(this.mediaCode, masterMediaUpsertDto.mediaCode) &&
        Objects.equals(this.amount, masterMediaUpsertDto.amount) &&
        Objects.equals(this.hexColor, masterMediaUpsertDto.hexColor) &&
        Objects.equals(this.memo, masterMediaUpsertDto.memo) &&
        Objects.equals(this.siteName, masterMediaUpsertDto.siteName) &&
        Objects.equals(this.managementLoginId, masterMediaUpsertDto.managementLoginId) &&
        Objects.equals(this.managementPwd, masterMediaUpsertDto.managementPwd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, mediaCode, amount, hexColor, memo, siteName, managementLoginId, managementPwd);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterMediaUpsertDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mediaCode: ").append(toIndentedString(mediaCode)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    hexColor: ").append(toIndentedString(hexColor)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    siteName: ").append(toIndentedString(siteName)).append("\n");
    sb.append("    managementLoginId: ").append(toIndentedString(managementLoginId)).append("\n");
    sb.append("    managementPwd: ").append(toIndentedString(managementPwd)).append("\n");
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
