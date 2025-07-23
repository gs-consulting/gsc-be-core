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
@Schema(name = "ApplicantInterviewUpsertDto", description = "面接日時を追加")
public class ApplicantInterviewUpsertDto {

  private String id = null;

  @NotBlank(message = "この項目は必須です。")
  private String categoryId;

  @NotBlank(message = "この項目は必須です。")
  private String startDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String endDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String picId;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantInterviewUpsertDto applicantInterviewUpsertDto = (ApplicantInterviewUpsertDto) o;
    return Objects.equals(this.id, applicantInterviewUpsertDto.id) &&
        Objects.equals(this.categoryId, applicantInterviewUpsertDto.categoryId) &&
        Objects.equals(this.startDateTime, applicantInterviewUpsertDto.startDateTime) &&
        Objects.equals(this.endDateTime, applicantInterviewUpsertDto.endDateTime) &&
        Objects.equals(this.picId, applicantInterviewUpsertDto.picId) &&
        Objects.equals(this.memo, applicantInterviewUpsertDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, categoryId, startDateTime, endDateTime, picId, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantInterviewUpsertDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    picId: ").append(toIndentedString(picId)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
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
