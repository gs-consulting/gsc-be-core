package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 案件一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectItemsDto", description = "案件一覧 ")
public class ProjectItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String projectNo;

  @NotBlank(message = "この項目は必須です。")
  private String projectName;

  private String statusId = null;

  private String occupation = null;

  private Integer totalApplies = null;

  private Integer totalInterviews = null;

  private Integer totalOffers = null;

  private Integer totalAgreements = null;

  private Integer goalApply = null;

  private Integer goalInterview = null;

  private Integer goalOffer = null;

  private Integer goalAgreement = null;

  
  private List<String> qualifications;

  private String workplace = null;

  private String abAdjustment = null;

  @NotBlank(message = "この項目は必須です。")
  private String memo;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime registeredDate;

  private Boolean isGroupedByOccupation = false;

  private Boolean isGroupedByBranch = false;

  private Boolean hasPermission;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectItemsDto projectItemsDto = (ProjectItemsDto) o;
    return Objects.equals(this.id, projectItemsDto.id) &&
        Objects.equals(this.projectNo, projectItemsDto.projectNo) &&
        Objects.equals(this.projectName, projectItemsDto.projectName) &&
        Objects.equals(this.statusId, projectItemsDto.statusId) &&
        Objects.equals(this.occupation, projectItemsDto.occupation) &&
        Objects.equals(this.totalApplies, projectItemsDto.totalApplies) &&
        Objects.equals(this.totalInterviews, projectItemsDto.totalInterviews) &&
        Objects.equals(this.totalOffers, projectItemsDto.totalOffers) &&
        Objects.equals(this.totalAgreements, projectItemsDto.totalAgreements) &&
        Objects.equals(this.goalApply, projectItemsDto.goalApply) &&
        Objects.equals(this.goalInterview, projectItemsDto.goalInterview) &&
        Objects.equals(this.goalOffer, projectItemsDto.goalOffer) &&
        Objects.equals(this.goalAgreement, projectItemsDto.goalAgreement) &&
        Objects.equals(this.qualifications, projectItemsDto.qualifications) &&
        Objects.equals(this.workplace, projectItemsDto.workplace) &&
        Objects.equals(this.abAdjustment, projectItemsDto.abAdjustment) &&
        Objects.equals(this.memo, projectItemsDto.memo) &&
        Objects.equals(this.registeredDate, projectItemsDto.registeredDate) &&
        Objects.equals(this.isGroupedByOccupation, projectItemsDto.isGroupedByOccupation) &&
        Objects.equals(this.isGroupedByBranch, projectItemsDto.isGroupedByBranch) &&
        Objects.equals(this.hasPermission, projectItemsDto.hasPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectNo, projectName, statusId, occupation, totalApplies, totalInterviews, totalOffers, totalAgreements, goalApply, goalInterview, goalOffer, goalAgreement, qualifications, workplace, abAdjustment, memo, registeredDate, isGroupedByOccupation, isGroupedByBranch, hasPermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    projectNo: ").append(toIndentedString(projectNo)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    statusId: ").append(toIndentedString(statusId)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    totalApplies: ").append(toIndentedString(totalApplies)).append("\n");
    sb.append("    totalInterviews: ").append(toIndentedString(totalInterviews)).append("\n");
    sb.append("    totalOffers: ").append(toIndentedString(totalOffers)).append("\n");
    sb.append("    totalAgreements: ").append(toIndentedString(totalAgreements)).append("\n");
    sb.append("    goalApply: ").append(toIndentedString(goalApply)).append("\n");
    sb.append("    goalInterview: ").append(toIndentedString(goalInterview)).append("\n");
    sb.append("    goalOffer: ").append(toIndentedString(goalOffer)).append("\n");
    sb.append("    goalAgreement: ").append(toIndentedString(goalAgreement)).append("\n");
    sb.append("    qualifications: ").append(toIndentedString(qualifications)).append("\n");
    sb.append("    workplace: ").append(toIndentedString(workplace)).append("\n");
    sb.append("    abAdjustment: ").append(toIndentedString(abAdjustment)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    registeredDate: ").append(toIndentedString(registeredDate)).append("\n");
    sb.append("    isGroupedByOccupation: ").append(toIndentedString(isGroupedByOccupation)).append("\n");
    sb.append("    isGroupedByBranch: ").append(toIndentedString(isGroupedByBranch)).append("\n");
    sb.append("    hasPermission: ").append(toIndentedString(hasPermission)).append("\n");
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
