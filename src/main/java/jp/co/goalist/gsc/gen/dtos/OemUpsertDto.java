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
 * # OEMグループ登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "OemUpsertDto", description = "# OEMグループ登録 ")
public class OemUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String oemName;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OemUpsertDto oemUpsertDto = (OemUpsertDto) o;
    return Objects.equals(this.oemName, oemUpsertDto.oemName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oemName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OemUpsertDto {\n");
    sb.append("    oemName: ").append(toIndentedString(oemName)).append("\n");
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
