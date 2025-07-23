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
 * 本日の採用状況
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "StatisticsTodayDto", description = "本日の採用状況")
public class StatisticsTodayDto {

  @NotNull(message = "この項目は必須です。")
  private Integer totalApplies;

  @NotNull(message = "この項目は必須です。")
  private Integer totalInterviews;

  @NotNull(message = "この項目は必須です。")
  private Integer totalOffers;

  @NotNull(message = "この項目は必須です。")
  private Integer totalAgreements;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticsTodayDto statisticsTodayDto = (StatisticsTodayDto) o;
    return Objects.equals(this.totalApplies, statisticsTodayDto.totalApplies) &&
        Objects.equals(this.totalInterviews, statisticsTodayDto.totalInterviews) &&
        Objects.equals(this.totalOffers, statisticsTodayDto.totalOffers) &&
        Objects.equals(this.totalAgreements, statisticsTodayDto.totalAgreements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalApplies, totalInterviews, totalOffers, totalAgreements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticsTodayDto {\n");
    sb.append("    totalApplies: ").append(toIndentedString(totalApplies)).append("\n");
    sb.append("    totalInterviews: ").append(toIndentedString(totalInterviews)).append("\n");
    sb.append("    totalOffers: ").append(toIndentedString(totalOffers)).append("\n");
    sb.append("    totalAgreements: ").append(toIndentedString(totalAgreements)).append("\n");
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
