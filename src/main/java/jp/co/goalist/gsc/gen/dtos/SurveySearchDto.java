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
 * SurveySearchDto
 */

@lombok.Getter
@lombok.Setter
public class SurveySearchDto {

  private Integer pageNumber = null;

  private Integer pageSize = null;

  private String arrangedBy = null;

  private String searchInput = null;

  private String applicantId = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveySearchDto surveySearchDto = (SurveySearchDto) o;
    return Objects.equals(this.pageNumber, surveySearchDto.pageNumber) &&
        Objects.equals(this.pageSize, surveySearchDto.pageSize) &&
        Objects.equals(this.arrangedBy, surveySearchDto.arrangedBy) &&
        Objects.equals(this.searchInput, surveySearchDto.searchInput) &&
        Objects.equals(this.applicantId, surveySearchDto.applicantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageNumber, pageSize, arrangedBy, searchInput, applicantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveySearchDto {\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    arrangedBy: ").append(toIndentedString(arrangedBy)).append("\n");
    sb.append("    searchInput: ").append(toIndentedString(searchInput)).append("\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
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
