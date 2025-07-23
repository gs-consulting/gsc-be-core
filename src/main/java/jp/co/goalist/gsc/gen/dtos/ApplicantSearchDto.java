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
 * # ApplicantSearchDto 応募者絞り込み 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ApplicantSearchDto", description = "# ApplicantSearchDto 応募者絞り込み ")
public class ApplicantSearchDto {

  private String applicantId = null;

  private String messageType = null;

  private String name = null;

  private Boolean isNameExactly;

  
  private List<String> genders;

  private Integer maxAge = null;

  private Integer minAge = null;

  
  private List<String> selectionStatusIds;

  
  private List<String> applicantStatuses;

  
  private List<String> masterMediaIds;

  private String projectName = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate applyStartDate = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate applyEndDate = null;

  
  private List<String> prefectures;

  private String city = null;

  private String address = null;

  
  private List<String> qualificationIds;

  
  private List<String> experienceIds;

  private Boolean isStatusNotChanged;

  private Boolean isUnread;

  private Integer pageNumber = null;

  private Integer pageSize = null;

  private String arrangedBy = null;

  private String blacklistId = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantSearchDto applicantSearchDto = (ApplicantSearchDto) o;
    return Objects.equals(this.applicantId, applicantSearchDto.applicantId) &&
        Objects.equals(this.messageType, applicantSearchDto.messageType) &&
        Objects.equals(this.name, applicantSearchDto.name) &&
        Objects.equals(this.isNameExactly, applicantSearchDto.isNameExactly) &&
        Objects.equals(this.genders, applicantSearchDto.genders) &&
        Objects.equals(this.maxAge, applicantSearchDto.maxAge) &&
        Objects.equals(this.minAge, applicantSearchDto.minAge) &&
        Objects.equals(this.selectionStatusIds, applicantSearchDto.selectionStatusIds) &&
        Objects.equals(this.applicantStatuses, applicantSearchDto.applicantStatuses) &&
        Objects.equals(this.masterMediaIds, applicantSearchDto.masterMediaIds) &&
        Objects.equals(this.projectName, applicantSearchDto.projectName) &&
        Objects.equals(this.applyStartDate, applicantSearchDto.applyStartDate) &&
        Objects.equals(this.applyEndDate, applicantSearchDto.applyEndDate) &&
        Objects.equals(this.prefectures, applicantSearchDto.prefectures) &&
        Objects.equals(this.city, applicantSearchDto.city) &&
        Objects.equals(this.address, applicantSearchDto.address) &&
        Objects.equals(this.qualificationIds, applicantSearchDto.qualificationIds) &&
        Objects.equals(this.experienceIds, applicantSearchDto.experienceIds) &&
        Objects.equals(this.isStatusNotChanged, applicantSearchDto.isStatusNotChanged) &&
        Objects.equals(this.isUnread, applicantSearchDto.isUnread) &&
        Objects.equals(this.pageNumber, applicantSearchDto.pageNumber) &&
        Objects.equals(this.pageSize, applicantSearchDto.pageSize) &&
        Objects.equals(this.arrangedBy, applicantSearchDto.arrangedBy) &&
        Objects.equals(this.blacklistId, applicantSearchDto.blacklistId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicantId, messageType, name, isNameExactly, genders, maxAge, minAge, selectionStatusIds, applicantStatuses, masterMediaIds, projectName, applyStartDate, applyEndDate, prefectures, city, address, qualificationIds, experienceIds, isStatusNotChanged, isUnread, pageNumber, pageSize, arrangedBy, blacklistId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantSearchDto {\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isNameExactly: ").append(toIndentedString(isNameExactly)).append("\n");
    sb.append("    genders: ").append(toIndentedString(genders)).append("\n");
    sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
    sb.append("    minAge: ").append(toIndentedString(minAge)).append("\n");
    sb.append("    selectionStatusIds: ").append(toIndentedString(selectionStatusIds)).append("\n");
    sb.append("    applicantStatuses: ").append(toIndentedString(applicantStatuses)).append("\n");
    sb.append("    masterMediaIds: ").append(toIndentedString(masterMediaIds)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    applyStartDate: ").append(toIndentedString(applyStartDate)).append("\n");
    sb.append("    applyEndDate: ").append(toIndentedString(applyEndDate)).append("\n");
    sb.append("    prefectures: ").append(toIndentedString(prefectures)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    qualificationIds: ").append(toIndentedString(qualificationIds)).append("\n");
    sb.append("    experienceIds: ").append(toIndentedString(experienceIds)).append("\n");
    sb.append("    isStatusNotChanged: ").append(toIndentedString(isStatusNotChanged)).append("\n");
    sb.append("    isUnread: ").append(toIndentedString(isUnread)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    arrangedBy: ").append(toIndentedString(arrangedBy)).append("\n");
    sb.append("    blacklistId: ").append(toIndentedString(blacklistId)).append("\n");
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
