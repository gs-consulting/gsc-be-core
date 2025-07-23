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
@Schema(name = "ClientBranchDataItemsDto", description = "案件権限_支店なし ")
public class ClientBranchDataItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  private String projectNo;

  @NotBlank(message = "この項目は必須です。")
  private String projectName;

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
    ClientBranchDataItemsDto clientBranchDataItemsDto = (ClientBranchDataItemsDto) o;
    return Objects.equals(this.id, clientBranchDataItemsDto.id) &&
        Objects.equals(this.projectNo, clientBranchDataItemsDto.projectNo) &&
        Objects.equals(this.projectName, clientBranchDataItemsDto.projectName) &&
        Objects.equals(this.permission, clientBranchDataItemsDto.permission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectNo, projectName, permission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientBranchDataItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    projectNo: ").append(toIndentedString(projectNo)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
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
