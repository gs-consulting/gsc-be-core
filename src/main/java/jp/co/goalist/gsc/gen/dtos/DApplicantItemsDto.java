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
 * 新規応募者一覧
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "DApplicantItemsDto", description = "新規応募者一覧")
public class DApplicantItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private Boolean isStatusNotChanged = false;

  private String gender = null;

  private Integer age = null;

  private String address = null;

  private String mediaName = null;

  private String projectName = null;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime registeredDate;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DApplicantItemsDto dapplicantItemsDto = (DApplicantItemsDto) o;
    return Objects.equals(this.id, dapplicantItemsDto.id) &&
        Objects.equals(this.fullName, dapplicantItemsDto.fullName) &&
        Objects.equals(this.isStatusNotChanged, dapplicantItemsDto.isStatusNotChanged) &&
        Objects.equals(this.gender, dapplicantItemsDto.gender) &&
        Objects.equals(this.age, dapplicantItemsDto.age) &&
        Objects.equals(this.address, dapplicantItemsDto.address) &&
        Objects.equals(this.mediaName, dapplicantItemsDto.mediaName) &&
        Objects.equals(this.projectName, dapplicantItemsDto.projectName) &&
        Objects.equals(this.registeredDate, dapplicantItemsDto.registeredDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, isStatusNotChanged, gender, age, address, mediaName, projectName, registeredDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DApplicantItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    isStatusNotChanged: ").append(toIndentedString(isStatusNotChanged)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    mediaName: ").append(toIndentedString(mediaName)).append("\n");
    sb.append("    projectName: ").append(toIndentedString(projectName)).append("\n");
    sb.append("    registeredDate: ").append(toIndentedString(registeredDate)).append("\n");
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
