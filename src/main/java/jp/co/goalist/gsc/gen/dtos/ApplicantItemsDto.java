package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 応募者一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ApplicantItemsDto", description = "応募者一覧 ")
public class ApplicantItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime registeredDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String gender = null;

  private Integer age = null;

  private String address = null;

  private String selectionStatusId = null;

  private String masterMediaName = null;

  private String picName = null;

  private String projectName = null;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime latestContactDateTime = null;

  private String memo = null;

  private Boolean isUnread;

  private Boolean hasTel;

  private Boolean hasEmail;

  private Boolean isStatusNotChanged = false;

  private Boolean isDuplicate;

  private Boolean isValid;

  private Boolean isDeletable;

  private Boolean isBlacklist;

  private String interviewDateTime = null;

  private Boolean hasPermission;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantItemsDto applicantItemsDto = (ApplicantItemsDto) o;
    return Objects.equals(this.id, applicantItemsDto.id) &&
        Objects.equals(this.registeredDateTime, applicantItemsDto.registeredDateTime) &&
        Objects.equals(this.fullName, applicantItemsDto.fullName) &&
        Objects.equals(this.gender, applicantItemsDto.gender) &&
        Objects.equals(this.age, applicantItemsDto.age) &&
        Objects.equals(this.address, applicantItemsDto.address) &&
        Objects.equals(this.selectionStatusId, applicantItemsDto.selectionStatusId) &&
        Objects.equals(this.masterMediaName, applicantItemsDto.masterMediaName) &&
        Objects.equals(this.picName, applicantItemsDto.picName) &&
        Objects.equals(this.projectName, applicantItemsDto.projectName) &&
        Objects.equals(this.latestContactDateTime, applicantItemsDto.latestContactDateTime) &&
        Objects.equals(this.memo, applicantItemsDto.memo) &&
        Objects.equals(this.isUnread, applicantItemsDto.isUnread) &&
        Objects.equals(this.hasTel, applicantItemsDto.hasTel) &&
        Objects.equals(this.hasEmail, applicantItemsDto.hasEmail) &&
        Objects.equals(this.isStatusNotChanged, applicantItemsDto.isStatusNotChanged) &&
        Objects.equals(this.isDuplicate, applicantItemsDto.isDuplicate) &&
        Objects.equals(this.isValid, applicantItemsDto.isValid) &&
        Objects.equals(this.isDeletable, applicantItemsDto.isDeletable) &&
        Objects.equals(this.isBlacklist, applicantItemsDto.isBlacklist) &&
        Objects.equals(this.interviewDateTime, applicantItemsDto.interviewDateTime) &&
        Objects.equals(this.hasPermission, applicantItemsDto.hasPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, registeredDateTime, fullName, gender, age, address, selectionStatusId, masterMediaName, picName, projectName, latestContactDateTime, memo, isUnread, hasTel, hasEmail, isStatusNotChanged, isDuplicate, isValid, isDeletable, isBlacklist, interviewDateTime, hasPermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    registeredDateTime: ").append(toIndentedString(registeredDateTime)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    selectionStatusId: ").append(toIndentedString(selectionStatusId)).append("\n");
    sb.append("    masterMediaName: ").append(toIndentedString(masterMediaName)).append("\n");
    sb.append("    picName: ").append(toIndentedString(picName)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    latestContactDateTime: ").append(toIndentedString(latestContactDateTime)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    isUnread: ").append(toIndentedString(isUnread)).append("\n");
    sb.append("    hasTel: ").append(toIndentedString(hasTel)).append("\n");
    sb.append("    hasEmail: ").append(toIndentedString(hasEmail)).append("\n");
    sb.append("    isStatusNotChanged: ").append(toIndentedString(isStatusNotChanged)).append("\n");
    sb.append("    isDuplicate: ").append(toIndentedString(isDuplicate)).append("\n");
    sb.append("    isValid: ").append(toIndentedString(isValid)).append("\n");
    sb.append("    isDeletable: ").append(toIndentedString(isDeletable)).append("\n");
    sb.append("    isBlacklist: ").append(toIndentedString(isBlacklist)).append("\n");
    sb.append("    interviewDateTime: ").append(toIndentedString(interviewDateTime)).append("\n");
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
