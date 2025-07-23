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
 * 職種権限一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectPermissionItemsDto", description = "職種権限一覧 ")
public class ProjectPermissionItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
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
    ProjectPermissionItemsDto projectPermissionItemsDto = (ProjectPermissionItemsDto) o;
    return Objects.equals(this.id, projectPermissionItemsDto.id) &&
        Objects.equals(this.projectNo, projectPermissionItemsDto.projectNo) &&
        Objects.equals(this.projectName, projectPermissionItemsDto.projectName) &&
        Objects.equals(this.permission, projectPermissionItemsDto.permission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectNo, projectName, permission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectPermissionItemsDto {\n");
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
