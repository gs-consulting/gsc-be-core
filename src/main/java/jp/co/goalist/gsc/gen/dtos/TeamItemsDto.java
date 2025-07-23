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
 * チーム一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "TeamItemsDto", description = "チーム一覧 ")
public class TeamItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String teamName;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TeamItemsDto teamItemsDto = (TeamItemsDto) o;
    return Objects.equals(this.id, teamItemsDto.id) &&
        Objects.equals(this.teamName, teamItemsDto.teamName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, teamName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TeamItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    teamName: ").append(toIndentedString(teamName)).append("\n");
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
