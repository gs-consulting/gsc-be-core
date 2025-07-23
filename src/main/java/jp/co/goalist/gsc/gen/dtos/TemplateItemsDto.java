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
 * テンプレート管理 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "TemplateItemsDto", description = "テンプレート管理 ")
public class TemplateItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String createdAt;

  @NotBlank(message = "この項目は必須です。")
  private String updatedAt;

  private Boolean isDeletable;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateItemsDto templateItemsDto = (TemplateItemsDto) o;
    return Objects.equals(this.id, templateItemsDto.id) &&
        Objects.equals(this.name, templateItemsDto.name) &&
        Objects.equals(this.createdAt, templateItemsDto.createdAt) &&
        Objects.equals(this.updatedAt, templateItemsDto.updatedAt) &&
        Objects.equals(this.isDeletable, templateItemsDto.isDeletable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, createdAt, updatedAt, isDeletable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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
