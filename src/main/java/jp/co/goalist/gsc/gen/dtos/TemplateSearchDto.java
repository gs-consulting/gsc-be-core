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
 * TemplateSearchDto
 */

@lombok.Getter
@lombok.Setter
public class TemplateSearchDto {

  private Integer pageNumber = null;

  private Integer pageSize = null;

  private String searchInput = null;

  private String arrangedBy = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateSearchDto templateSearchDto = (TemplateSearchDto) o;
    return Objects.equals(this.pageNumber, templateSearchDto.pageNumber) &&
        Objects.equals(this.pageSize, templateSearchDto.pageSize) &&
        Objects.equals(this.searchInput, templateSearchDto.searchInput) &&
        Objects.equals(this.arrangedBy, templateSearchDto.arrangedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageNumber, pageSize, searchInput, arrangedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateSearchDto {\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    searchInput: ").append(toIndentedString(searchInput)).append("\n");
    sb.append("    arrangedBy: ").append(toIndentedString(arrangedBy)).append("\n");
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
