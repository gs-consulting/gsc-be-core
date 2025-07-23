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
 * 案件権限_支店なし 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientBranchRestrictionUpsertDto", description = "案件権限_支店なし ")
public class ClientBranchRestrictionUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String permission;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientBranchRestrictionUpsertDto clientBranchRestrictionUpsertDto = (ClientBranchRestrictionUpsertDto) o;
    return Objects.equals(this.id, clientBranchRestrictionUpsertDto.id) &&
        Objects.equals(this.permission, clientBranchRestrictionUpsertDto.permission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, permission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientBranchRestrictionUpsertDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
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
