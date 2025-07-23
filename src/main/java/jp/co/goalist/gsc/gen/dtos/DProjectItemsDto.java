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
 * お知らせ設定一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "DProjectItemsDto", description = "お知らせ設定一覧 ")
public class DProjectItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String registeredDate;

  @NotBlank(message = "この項目は必須です。")
  private String projectNo;

  private String projectName = null;

  private String statusName = null;

  @NotBlank(message = "この項目は必須です。")
  private String occupation;

  private Integer totalApplies = null;

  private Integer totalInterviews = null;

  private Integer totalOffers = null;

  private Integer totalAgreements = null;

  private Integer goalApply = null;

  private Integer goalInterview = null;

  private Integer goalOffer = null;

  private Integer goalAgreement = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DProjectItemsDto dprojectItemsDto = (DProjectItemsDto) o;
    return Objects.equals(this.id, dprojectItemsDto.id) &&
        Objects.equals(this.registeredDate, dprojectItemsDto.registeredDate) &&
        Objects.equals(this.projectNo, dprojectItemsDto.projectNo) &&
        Objects.equals(this.projectName, dprojectItemsDto.projectName) &&
        Objects.equals(this.statusName, dprojectItemsDto.statusName) &&
        Objects.equals(this.occupation, dprojectItemsDto.occupation) &&
        Objects.equals(this.totalApplies, dprojectItemsDto.totalApplies) &&
        Objects.equals(this.totalInterviews, dprojectItemsDto.totalInterviews) &&
        Objects.equals(this.totalOffers, dprojectItemsDto.totalOffers) &&
        Objects.equals(this.totalAgreements, dprojectItemsDto.totalAgreements) &&
        Objects.equals(this.goalApply, dprojectItemsDto.goalApply) &&
        Objects.equals(this.goalInterview, dprojectItemsDto.goalInterview) &&
        Objects.equals(this.goalOffer, dprojectItemsDto.goalOffer) &&
        Objects.equals(this.goalAgreement, dprojectItemsDto.goalAgreement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, registeredDate, projectNo, projectName, statusName, occupation, totalApplies, totalInterviews, totalOffers, totalAgreements, goalApply, goalInterview, goalOffer, goalAgreement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DProjectItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    registeredDate: ").append(toIndentedString(registeredDate)).append("\n");
    sb.append("    projectNo: ").append(toIndentedString(projectNo)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    statusName: ").append(toIndentedString(statusName)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    totalApplies: ").append(toIndentedString(totalApplies)).append("\n");
    sb.append("    totalInterviews: ").append(toIndentedString(totalInterviews)).append("\n");
    sb.append("    totalOffers: ").append(toIndentedString(totalOffers)).append("\n");
    sb.append("    totalAgreements: ").append(toIndentedString(totalAgreements)).append("\n");
    sb.append("    goalApply: ").append(toIndentedString(goalApply)).append("\n");
    sb.append("    goalInterview: ").append(toIndentedString(goalInterview)).append("\n");
    sb.append("    goalOffer: ").append(toIndentedString(goalOffer)).append("\n");
    sb.append("    goalAgreement: ").append(toIndentedString(goalAgreement)).append("\n");
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
