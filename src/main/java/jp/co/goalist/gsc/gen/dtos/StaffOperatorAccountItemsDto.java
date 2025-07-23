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
 * 運営のアカウント情報
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StaffOperatorAccountItemsDto", description = "運営のアカウント情報")
public class StaffOperatorAccountItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  
  private List<String> teams = new ArrayList<>();

  private Boolean isExpired;

  private Boolean isDeletable;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StaffOperatorAccountItemsDto staffOperatorAccountItemsDto = (StaffOperatorAccountItemsDto) o;
    return Objects.equals(this.id, staffOperatorAccountItemsDto.id) &&
        Objects.equals(this.fullName, staffOperatorAccountItemsDto.fullName) &&
        Objects.equals(this.email, staffOperatorAccountItemsDto.email) &&
        Objects.equals(this.teams, staffOperatorAccountItemsDto.teams) &&
        Objects.equals(this.isExpired, staffOperatorAccountItemsDto.isExpired) &&
        Objects.equals(this.isDeletable, staffOperatorAccountItemsDto.isDeletable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, email, teams, isExpired, isDeletable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StaffOperatorAccountItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    teams: ").append(toIndentedString(teams)).append("\n");
    sb.append("    isExpired: ").append(toIndentedString(isExpired)).append("\n");
    sb.append("    isDeletable: ").append(toIndentedString(isDeletable)).append("\n");
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
