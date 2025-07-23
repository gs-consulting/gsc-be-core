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
 * チーム登録
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "TeamUpsertDto", description = "チーム登録")
public class TeamUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
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
    TeamUpsertDto teamUpsertDto = (TeamUpsertDto) o;
    return Objects.equals(this.name, teamUpsertDto.name) &&
        Objects.equals(this.furiganaName, teamUpsertDto.furiganaName) &&
        Objects.equals(this.teamCode, teamUpsertDto.teamCode) &&
        Objects.equals(this.staffIds, teamUpsertDto.staffIds) &&
        Objects.equals(this.memo, teamUpsertDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, furiganaName, teamCode, staffIds, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TeamUpsertDto {\n");
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
