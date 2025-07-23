package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.SurveyAnswersItemsDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 応募者リストの回答 
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "SurveyAnswersListDto", description = "応募者リストの回答 ")
public class SurveyAnswersListDto {

  @NotNull(message = "この項目は必須です。")
  private Integer page = 1;

  @NotNull(message = "この項目は必須です。")
  private Integer limit = 10;

  @NotNull(message = "この項目は必須です。")
  private Long total = 0l;

  @NotNull(message = "この項目は必須です。")
  private Integer totalQuestions;

  @Valid
  private List<@Valid SurveyAnswersItemsDto> items = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyAnswersListDto surveyAnswersListDto = (SurveyAnswersListDto) o;
    return Objects.equals(this.page, surveyAnswersListDto.page) &&
        Objects.equals(this.limit, surveyAnswersListDto.limit) &&
        Objects.equals(this.total, surveyAnswersListDto.total) &&
        Objects.equals(this.totalQuestions, surveyAnswersListDto.totalQuestions) &&
        Objects.equals(this.items, surveyAnswersListDto.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, limit, total, totalQuestions, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyAnswersListDto {\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    totalQuestions: ").append(toIndentedString(totalQuestions)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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
