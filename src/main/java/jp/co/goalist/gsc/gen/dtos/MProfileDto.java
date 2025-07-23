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
 * 応募者情報 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MProfileDto", description = "応募者情報 ")
public class MProfileDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String furiganaName = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate birthday = null;

  private Integer age = null;

  private String gender = null;

  private String email = null;

  private String tel = null;

  private String prefecture = null;

  private String city = null;

  private String homeAddress = null;

  private String occupation = null;

  private String selectionStatus = null;

  
  private List<String> qualifications;

  
  private List<String> experiences;

  @NotBlank(message = "この項目は必須です。")
  private String manuscriptUrl;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate joinDate = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MProfileDto mprofileDto = (MProfileDto) o;
    return Objects.equals(this.id, mprofileDto.id) &&
        Objects.equals(this.fullName, mprofileDto.fullName) &&
        Objects.equals(this.furiganaName, mprofileDto.furiganaName) &&
        Objects.equals(this.birthday, mprofileDto.birthday) &&
        Objects.equals(this.age, mprofileDto.age) &&
        Objects.equals(this.gender, mprofileDto.gender) &&
        Objects.equals(this.email, mprofileDto.email) &&
        Objects.equals(this.tel, mprofileDto.tel) &&
        Objects.equals(this.prefecture, mprofileDto.prefecture) &&
        Objects.equals(this.city, mprofileDto.city) &&
        Objects.equals(this.homeAddress, mprofileDto.homeAddress) &&
        Objects.equals(this.occupation, mprofileDto.occupation) &&
        Objects.equals(this.selectionStatus, mprofileDto.selectionStatus) &&
        Objects.equals(this.qualifications, mprofileDto.qualifications) &&
        Objects.equals(this.experiences, mprofileDto.experiences) &&
        Objects.equals(this.manuscriptUrl, mprofileDto.manuscriptUrl) &&
        Objects.equals(this.joinDate, mprofileDto.joinDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, furiganaName, birthday, age, gender, email, tel, prefecture, city, homeAddress, occupation, selectionStatus, qualifications, experiences, manuscriptUrl, joinDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MProfileDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    selectionStatus: ").append(toIndentedString(selectionStatus)).append("\n");
    sb.append("    qualifications: ").append(toIndentedString(qualifications)).append("\n");
    sb.append("    experiences: ").append(toIndentedString(experiences)).append("\n");
    sb.append("    manuscriptUrl: ").append(toIndentedString(manuscriptUrl)).append("\n");
    sb.append("    joinDate: ").append(toIndentedString(joinDate)).append("\n");
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
