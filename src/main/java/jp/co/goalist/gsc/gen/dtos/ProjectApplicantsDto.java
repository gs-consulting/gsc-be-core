package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.gen.dtos.ProjectApplicantListDto;
import jp.co.goalist.gsc.gen.dtos.ProjectInfoDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 案件の応募者一覧 
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "ProjectApplicantsDto", description = "案件の応募者一覧 ")
public class ProjectApplicantsDto {

  private ProjectInfoDto project;

  private ProjectApplicantListDto applicants;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectApplicantsDto projectApplicantsDto = (ProjectApplicantsDto) o;
    return Objects.equals(this.project, projectApplicantsDto.project) &&
        Objects.equals(this.applicants, projectApplicantsDto.applicants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(project, applicants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectApplicantsDto {\n");
    sb.append("    project: ").append(toIndentedString(project)).append("\n");
    sb.append("    applicants: ").append(toIndentedString(applicants)).append("\n");
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
