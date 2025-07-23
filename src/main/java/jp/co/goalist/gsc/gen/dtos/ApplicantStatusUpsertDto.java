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
 * 応募者のステータス編集 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ApplicantStatusUpsertDto", description = "応募者のステータス編集 ")
public class ApplicantStatusUpsertDto {

  private String statusId = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantStatusUpsertDto applicantStatusUpsertDto = (ApplicantStatusUpsertDto) o;
    return Objects.equals(this.statusId, applicantStatusUpsertDto.statusId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantStatusUpsertDto {\n");
    sb.append("    statusId: ").append(toIndentedString(statusId)).append("\n");
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
