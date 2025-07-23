package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * OEMアカウント情報
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "OemAccountItemsDto", description = "OEMアカウント情報")
public class OemAccountItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  
  private List<String> teams;

  private String tel = null;

  private Boolean isExpired;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OemAccountItemsDto oemAccountItemsDto = (OemAccountItemsDto) o;
    return Objects.equals(this.id, oemAccountItemsDto.id) &&
        Objects.equals(this.fullName, oemAccountItemsDto.fullName) &&
        Objects.equals(this.teams, oemAccountItemsDto.teams) &&
        Objects.equals(this.tel, oemAccountItemsDto.tel) &&
        Objects.equals(this.isExpired, oemAccountItemsDto.isExpired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, teams, tel, isExpired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OemAccountItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    isExpired: ").append(toIndentedString(isExpired)).append("\n");
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
