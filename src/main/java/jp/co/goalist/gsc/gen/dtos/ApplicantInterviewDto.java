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
 * 面接日時を追加
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ApplicantInterviewDto", description = "面接日時を追加")
public class ApplicantInterviewDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String categoryId;

  @NotBlank(message = "この項目は必須です。")
  private String startDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String endDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String picId;

  private String memo = null;

  @NotBlank(message = "この項目は必須です。")
  private String applicantId;

  private Boolean isDeletable;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantInterviewDto applicantInterviewDto = (ApplicantInterviewDto) o;
    return Objects.equals(this.id, applicantInterviewDto.id) &&
        Objects.equals(this.categoryId, applicantInterviewDto.categoryId) &&
        Objects.equals(this.startDateTime, applicantInterviewDto.startDateTime) &&
        Objects.equals(this.endDateTime, applicantInterviewDto.endDateTime) &&
        Objects.equals(this.picId, applicantInterviewDto.picId) &&
        Objects.equals(this.memo, applicantInterviewDto.memo) &&
        Objects.equals(this.applicantId, applicantInterviewDto.applicantId) &&
        Objects.equals(this.isDeletable, applicantInterviewDto.isDeletable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, categoryId, startDateTime, endDateTime, picId, memo, applicantId, isDeletable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantInterviewDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    picId: ").append(toIndentedString(picId)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
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
