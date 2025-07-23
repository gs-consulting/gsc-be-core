package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 媒体費レポートの検索ボックス 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MediaReportSearchBoxDto", description = "媒体費レポートの検索ボックス ")
public class MediaReportSearchBoxDto {

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate startDate;

  @NotNull(message = "この項目は必須です。")
  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate endDate;

  @NotBlank(message = "この項目は必須です。")
  private String dateType;

  @NotBlank(message = "この項目は必須です。")
  private String dataType;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaReportSearchBoxDto mediaReportSearchBoxDto = (MediaReportSearchBoxDto) o;
    return Objects.equals(this.startDate, mediaReportSearchBoxDto.startDate) &&
        Objects.equals(this.endDate, mediaReportSearchBoxDto.endDate) &&
        Objects.equals(this.dateType, mediaReportSearchBoxDto.dateType) &&
        Objects.equals(this.dataType, mediaReportSearchBoxDto.dataType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate, dateType, dataType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaReportSearchBoxDto {\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    dateType: ").append(toIndentedString(dateType)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
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
