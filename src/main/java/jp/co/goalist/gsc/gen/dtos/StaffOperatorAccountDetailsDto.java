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
 * 運営のアカウント詳細
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StaffOperatorAccountDetailsDto", description = "運営のアカウント詳細")
public class StaffOperatorAccountDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  
  private List<String> teamIds;

  
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
    StaffOperatorAccountDetailsDto staffOperatorAccountDetailsDto = (StaffOperatorAccountDetailsDto) o;
    return Objects.equals(this.id, staffOperatorAccountDetailsDto.id) &&
        Objects.equals(this.email, staffOperatorAccountDetailsDto.email) &&
        Objects.equals(this.fullName, staffOperatorAccountDetailsDto.fullName) &&
        Objects.equals(this.teamIds, staffOperatorAccountDetailsDto.teamIds) &&
        Objects.equals(this.permissions, staffOperatorAccountDetailsDto.permissions) &&
        Objects.equals(this.isRestricted, staffOperatorAccountDetailsDto.isRestricted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, fullName, teamIds, permissions, isRestricted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StaffOperatorAccountDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    teamIds: ").append(toIndentedString(teamIds)).append("\n");
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
