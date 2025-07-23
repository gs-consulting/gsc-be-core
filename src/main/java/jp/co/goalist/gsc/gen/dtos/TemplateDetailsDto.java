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
 * テンプレート詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "TemplateDetailsDto", description = "テンプレート詳細 ")
public class TemplateDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String content;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateDetailsDto templateDetailsDto = (TemplateDetailsDto) o;
    return Objects.equals(this.id, templateDetailsDto.id) &&
        Objects.equals(this.name, templateDetailsDto.name) &&
        Objects.equals(this.content, templateDetailsDto.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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
