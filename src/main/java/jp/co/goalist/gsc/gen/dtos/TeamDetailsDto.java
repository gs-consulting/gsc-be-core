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
 * チーム詳細
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "TeamDetailsDto", description = "チーム詳細")
public class TeamDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  private String teamCode = null;

  
  private List<String> staffIds;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TeamDetailsDto teamDetailsDto = (TeamDetailsDto) o;
    return Objects.equals(this.id, teamDetailsDto.id) &&
        Objects.equals(this.name, teamDetailsDto.name) &&
        Objects.equals(this.furiganaName, teamDetailsDto.furiganaName) &&
        Objects.equals(this.teamCode, teamDetailsDto.teamCode) &&
        Objects.equals(this.staffIds, teamDetailsDto.staffIds) &&
        Objects.equals(this.memo, teamDetailsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, furiganaName, teamCode, staffIds, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TeamDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    teamCode: ").append(toIndentedString(teamCode)).append("\n");
    sb.append("    staffIds: ").append(toIndentedString(staffIds)).append("\n");
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
