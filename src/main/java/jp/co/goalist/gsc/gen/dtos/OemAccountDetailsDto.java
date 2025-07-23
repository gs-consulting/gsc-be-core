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
@Schema(name = "OemAccountDetailsDto", description = "OEMアカウント情報")
public class OemAccountDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  
  private List<String> teamIds;

  private String tel = null;

  private String faxCode = null;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OemAccountDetailsDto oemAccountDetailsDto = (OemAccountDetailsDto) o;
    return Objects.equals(this.id, oemAccountDetailsDto.id) &&
        Objects.equals(this.email, oemAccountDetailsDto.email) &&
        Objects.equals(this.fullName, oemAccountDetailsDto.fullName) &&
        Objects.equals(this.furiganaName, oemAccountDetailsDto.furiganaName) &&
        Objects.equals(this.teamIds, oemAccountDetailsDto.teamIds) &&
        Objects.equals(this.tel, oemAccountDetailsDto.tel) &&
        Objects.equals(this.faxCode, oemAccountDetailsDto.faxCode) &&
        Objects.equals(this.memo, oemAccountDetailsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, fullName, furiganaName, teamIds, tel, faxCode, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OemAccountDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    teamIds: ").append(toIndentedString(teamIds)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
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
