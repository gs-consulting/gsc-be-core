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
 * 案件の応募者一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ProjectApplicantItemsDto", description = "案件の応募者一覧 ")
public class ProjectApplicantItemsDto {

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectApplicantItemsDto projectApplicantItemsDto = (ProjectApplicantItemsDto) o;
    return Objects.equals(this.id, projectApplicantItemsDto.id) &&
        Objects.equals(this.registeredDateTime, projectApplicantItemsDto.registeredDateTime) &&
        Objects.equals(this.fullName, projectApplicantItemsDto.fullName) &&
        Objects.equals(this.gender, projectApplicantItemsDto.gender) &&
        Objects.equals(this.age, projectApplicantItemsDto.age) &&
        Objects.equals(this.address, projectApplicantItemsDto.address) &&
        Objects.equals(this.selectionStatusId, projectApplicantItemsDto.selectionStatusId) &&
        Objects.equals(this.masterMediaName, projectApplicantItemsDto.masterMediaName) &&
        Objects.equals(this.picName, projectApplicantItemsDto.picName) &&
        Objects.equals(this.projectName, projectApplicantItemsDto.projectName) &&
        Objects.equals(this.latestContactDateTime, projectApplicantItemsDto.latestContactDateTime) &&
        Objects.equals(this.memo, projectApplicantItemsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, registeredDateTime, fullName, gender, age, address, selectionStatusId, masterMediaName, picName, projectName, latestContactDateTime, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectApplicantItemsDto {\n");
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
