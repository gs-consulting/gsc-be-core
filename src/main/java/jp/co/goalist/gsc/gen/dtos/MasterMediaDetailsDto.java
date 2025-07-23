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
 * 媒体マスタ詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MasterMediaDetailsDto", description = "媒体マスタ詳細 ")
public class MasterMediaDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

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
    MasterMediaDetailsDto masterMediaDetailsDto = (MasterMediaDetailsDto) o;
    return Objects.equals(this.id, masterMediaDetailsDto.id) &&
        Objects.equals(this.name, masterMediaDetailsDto.name) &&
        Objects.equals(this.mediaCode, masterMediaDetailsDto.mediaCode) &&
        Objects.equals(this.amount, masterMediaDetailsDto.amount) &&
        Objects.equals(this.hexColor, masterMediaDetailsDto.hexColor) &&
        Objects.equals(this.memo, masterMediaDetailsDto.memo) &&
        Objects.equals(this.siteName, masterMediaDetailsDto.siteName) &&
        Objects.equals(this.managementLoginId, masterMediaDetailsDto.managementLoginId) &&
        Objects.equals(this.managementPwd, masterMediaDetailsDto.managementPwd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, mediaCode, amount, hexColor, memo, siteName, managementLoginId, managementPwd);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterMediaDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
