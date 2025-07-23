package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
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
 * 案件の案件絞り込み 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectSearchBoxDto", description = "案件の案件絞り込み ")
public class ProjectSearchBoxDto {

  private String projectNo = null;

  private String projectName = null;

  
  private List<String> statusIds;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate registeredStartDate = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate registeredEndDate = null;

  
  private List<String> occupationIds;

  private String isShiftSystem = null;

  private String experience = null;

  
  private List<String> qualificationIds;

  private String qualificationNotes = null;

  private String prefecture = null;

  private String city = null;

  private String ward = null;

  private String holiday = null;

  private String benefit = null;

  
  private List<String> branchIds;

  private String storeName = null;

  private String workingPeriodId = null;

  private String abAdjustment = null;

  private Integer pageNumber = null;

  private Integer pageSize = null;

  private String arrangedBy = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectSearchBoxDto projectSearchBoxDto = (ProjectSearchBoxDto) o;
    return Objects.equals(this.projectNo, projectSearchBoxDto.projectNo) &&
        Objects.equals(this.projectName, projectSearchBoxDto.projectName) &&
        Objects.equals(this.statusIds, projectSearchBoxDto.statusIds) &&
        Objects.equals(this.registeredStartDate, projectSearchBoxDto.registeredStartDate) &&
        Objects.equals(this.registeredEndDate, projectSearchBoxDto.registeredEndDate) &&
        Objects.equals(this.occupationIds, projectSearchBoxDto.occupationIds) &&
        Objects.equals(this.isShiftSystem, projectSearchBoxDto.isShiftSystem) &&
        Objects.equals(this.experience, projectSearchBoxDto.experience) &&
        Objects.equals(this.qualificationIds, projectSearchBoxDto.qualificationIds) &&
        Objects.equals(this.qualificationNotes, projectSearchBoxDto.qualificationNotes) &&
        Objects.equals(this.prefecture, projectSearchBoxDto.prefecture) &&
        Objects.equals(this.city, projectSearchBoxDto.city) &&
        Objects.equals(this.ward, projectSearchBoxDto.ward) &&
        Objects.equals(this.holiday, projectSearchBoxDto.holiday) &&
        Objects.equals(this.benefit, projectSearchBoxDto.benefit) &&
        Objects.equals(this.branchIds, projectSearchBoxDto.branchIds) &&
        Objects.equals(this.storeName, projectSearchBoxDto.storeName) &&
        Objects.equals(this.workingPeriodId, projectSearchBoxDto.workingPeriodId) &&
        Objects.equals(this.abAdjustment, projectSearchBoxDto.abAdjustment) &&
        Objects.equals(this.pageNumber, projectSearchBoxDto.pageNumber) &&
        Objects.equals(this.pageSize, projectSearchBoxDto.pageSize) &&
        Objects.equals(this.arrangedBy, projectSearchBoxDto.arrangedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectNo, projectName, statusIds, registeredStartDate, registeredEndDate, occupationIds, isShiftSystem, experience, qualificationIds, qualificationNotes, prefecture, city, ward, holiday, benefit, branchIds, storeName, workingPeriodId, abAdjustment, pageNumber, pageSize, arrangedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectSearchBoxDto {\n");
    sb.append("    projectNo: ").append(toIndentedString(projectNo)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    statusIds: ").append(toIndentedString(statusIds)).append("\n");
    sb.append("    registeredStartDate: ").append(toIndentedString(registeredStartDate)).append("\n");
    sb.append("    registeredEndDate: ").append(toIndentedString(registeredEndDate)).append("\n");
    sb.append("    occupationIds: ").append(toIndentedString(occupationIds)).append("\n");
    sb.append("    isShiftSystem: ").append(toIndentedString(isShiftSystem)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    qualificationIds: ").append(toIndentedString(qualificationIds)).append("\n");
    sb.append("    qualificationNotes: ").append(toIndentedString(qualificationNotes)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    ward: ").append(toIndentedString(ward)).append("\n");
    sb.append("    holiday: ").append(toIndentedString(holiday)).append("\n");
    sb.append("    benefit: ").append(toIndentedString(benefit)).append("\n");
    sb.append("    branchIds: ").append(toIndentedString(branchIds)).append("\n");
    sb.append("    storeName: ").append(toIndentedString(storeName)).append("\n");
    sb.append("    workingPeriodId: ").append(toIndentedString(workingPeriodId)).append("\n");
    sb.append("    abAdjustment: ").append(toIndentedString(abAdjustment)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    arrangedBy: ").append(toIndentedString(arrangedBy)).append("\n");
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
