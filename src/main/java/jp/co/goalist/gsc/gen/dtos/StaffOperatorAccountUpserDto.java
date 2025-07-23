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
 * 運営のアカウント登録
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StaffOperatorAccountUpserDto", description = "運営のアカウント登録")
public class StaffOperatorAccountUpserDto {

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  
  private List<String> teamIds;

  
  private List<String> permissions;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StaffOperatorAccountUpserDto staffOperatorAccountUpserDto = (StaffOperatorAccountUpserDto) o;
    return Objects.equals(this.email, staffOperatorAccountUpserDto.email) &&
        Objects.equals(this.fullName, staffOperatorAccountUpserDto.fullName) &&
        Objects.equals(this.teamIds, staffOperatorAccountUpserDto.teamIds) &&
        Objects.equals(this.permissions, staffOperatorAccountUpserDto.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, fullName, teamIds, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StaffOperatorAccountUpserDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    teamIds: ").append(toIndentedString(teamIds)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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
