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
 * 外部からの郵便番号
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MPostCodeDto", description = "外部からの郵便番号")
public class MPostCodeDto {

  @NotBlank(message = "この項目は必須です。")
  private String address1;

  @NotBlank(message = "この項目は必須です。")
  private String address2;

  @NotBlank(message = "この項目は必須です。")
  private String addressName2;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MPostCodeDto mpostCodeDto = (MPostCodeDto) o;
    return Objects.equals(this.address1, mpostCodeDto.address1) &&
        Objects.equals(this.address2, mpostCodeDto.address2) &&
        Objects.equals(this.addressName2, mpostCodeDto.addressName2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address1, address2, addressName2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MPostCodeDto {\n");
    sb.append("    address1: ").append(toIndentedString(address1)).append("\n");
    sb.append("    address2: ").append(toIndentedString(address2)).append("\n");
    sb.append("    addressName2: ").append(toIndentedString(addressName2)).append("\n");
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
