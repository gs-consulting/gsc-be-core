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
 * OEMアカウント詳細
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StaffOemAccountDetailsDto", description = "OEMアカウント詳細")
public class StaffOemAccountDetailsDto {

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

  
  private List<String> permissions;

  private Boolean isRestricted;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StaffOemAccountDetailsDto staffOemAccountDetailsDto = (StaffOemAccountDetailsDto) o;
    return Objects.equals(this.id, staffOemAccountDetailsDto.id) &&
        Objects.equals(this.email, staffOemAccountDetailsDto.email) &&
        Objects.equals(this.fullName, staffOemAccountDetailsDto.fullName) &&
        Objects.equals(this.furiganaName, staffOemAccountDetailsDto.furiganaName) &&
        Objects.equals(this.teamIds, staffOemAccountDetailsDto.teamIds) &&
        Objects.equals(this.tel, staffOemAccountDetailsDto.tel) &&
        Objects.equals(this.faxCode, staffOemAccountDetailsDto.faxCode) &&
        Objects.equals(this.memo, staffOemAccountDetailsDto.memo) &&
        Objects.equals(this.permissions, staffOemAccountDetailsDto.permissions) &&
        Objects.equals(this.isRestricted, staffOemAccountDetailsDto.isRestricted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, fullName, furiganaName, teamIds, tel, faxCode, memo, permissions, isRestricted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StaffOemAccountDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    teamIds: ").append(toIndentedString(teamIds)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    isRestricted: ").append(toIndentedString(isRestricted)).append("\n");
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
