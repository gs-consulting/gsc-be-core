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
 * 案件の情報のみ 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectInfoDto", description = "案件の情報のみ ")
public class ProjectInfoDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String projectNo;

  @NotBlank(message = "この項目は必須です。")
  private String projectName;

  private String statusName = null;

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

  @NotBlank(message = "この項目は必須です。")
  private String flowType;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectInfoDto projectInfoDto = (ProjectInfoDto) o;
    return Objects.equals(this.id, projectInfoDto.id) &&
        Objects.equals(this.projectNo, projectInfoDto.projectNo) &&
        Objects.equals(this.projectName, projectInfoDto.projectName) &&
        Objects.equals(this.statusName, projectInfoDto.statusName) &&
        Objects.equals(this.occupation, projectInfoDto.occupation) &&
        Objects.equals(this.totalApplies, projectInfoDto.totalApplies) &&
        Objects.equals(this.totalInterviews, projectInfoDto.totalInterviews) &&
        Objects.equals(this.totalOffers, projectInfoDto.totalOffers) &&
        Objects.equals(this.totalAgreements, projectInfoDto.totalAgreements) &&
        Objects.equals(this.goalApply, projectInfoDto.goalApply) &&
        Objects.equals(this.goalInterview, projectInfoDto.goalInterview) &&
        Objects.equals(this.goalOffer, projectInfoDto.goalOffer) &&
        Objects.equals(this.goalAgreement, projectInfoDto.goalAgreement) &&
        Objects.equals(this.qualifications, projectInfoDto.qualifications) &&
        Objects.equals(this.workplace, projectInfoDto.workplace) &&
        Objects.equals(this.abAdjustment, projectInfoDto.abAdjustment) &&
        Objects.equals(this.memo, projectInfoDto.memo) &&
        Objects.equals(this.registeredDate, projectInfoDto.registeredDate) &&
        Objects.equals(this.flowType, projectInfoDto.flowType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectNo, projectName, statusName, occupation, totalApplies, totalInterviews, totalOffers, totalAgreements, goalApply, goalInterview, goalOffer, goalAgreement, qualifications, workplace, abAdjustment, memo, registeredDate, flowType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectInfoDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
    sb.append("    qualifications: ").append(toIndentedString(qualifications)).append("\n");
    sb.append("    workplace: ").append(toIndentedString(workplace)).append("\n");
    sb.append("    abAdjustment: ").append(toIndentedString(abAdjustment)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    registeredDate: ").append(toIndentedString(registeredDate)).append("\n");
    sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
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
