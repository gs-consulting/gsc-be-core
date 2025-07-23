package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.ApplicantInterviewUpsertDto;
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
@Schema(name = "ApplicantUpsertDto", description = "応募者情報 ")
public class ApplicantUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
  private String furiganaName = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate birthday = null;

  private String gender = null;

  @NotBlank(message = "この項目は必須です。")
  private String projectId;

  private String mediaId = null;

  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email = null;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.PostCodeConstraint
  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  private String homeAddress = null;

  private String occupation = null;

  private String selectionStatusId = null;

  
  private List<String> qualificationIds;

  
  private List<String> experienceIds;

  private String picId = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate joinDate = null;

  @Valid
  private List<@Valid ApplicantInterviewUpsertDto> interviews;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantUpsertDto applicantUpsertDto = (ApplicantUpsertDto) o;
    return Objects.equals(this.fullName, applicantUpsertDto.fullName) &&
        Objects.equals(this.furiganaName, applicantUpsertDto.furiganaName) &&
        Objects.equals(this.birthday, applicantUpsertDto.birthday) &&
        Objects.equals(this.gender, applicantUpsertDto.gender) &&
        Objects.equals(this.projectId, applicantUpsertDto.projectId) &&
        Objects.equals(this.mediaId, applicantUpsertDto.mediaId) &&
        Objects.equals(this.email, applicantUpsertDto.email) &&
        Objects.equals(this.tel, applicantUpsertDto.tel) &&
        Objects.equals(this.postCode, applicantUpsertDto.postCode) &&
        Objects.equals(this.prefecture, applicantUpsertDto.prefecture) &&
        Objects.equals(this.city, applicantUpsertDto.city) &&
        Objects.equals(this.homeAddress, applicantUpsertDto.homeAddress) &&
        Objects.equals(this.occupation, applicantUpsertDto.occupation) &&
        Objects.equals(this.selectionStatusId, applicantUpsertDto.selectionStatusId) &&
        Objects.equals(this.qualificationIds, applicantUpsertDto.qualificationIds) &&
        Objects.equals(this.experienceIds, applicantUpsertDto.experienceIds) &&
        Objects.equals(this.picId, applicantUpsertDto.picId) &&
        Objects.equals(this.joinDate, applicantUpsertDto.joinDate) &&
        Objects.equals(this.interviews, applicantUpsertDto.interviews);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, furiganaName, birthday, gender, projectId, mediaId, email, tel, postCode, prefecture, city, homeAddress, occupation, selectionStatusId, qualificationIds, experienceIds, picId, joinDate, interviews);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantUpsertDto {\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    projectId: ").append(toIndentedString(projectId)).append("\n");
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    selectionStatusId: ").append(toIndentedString(selectionStatusId)).append("\n");
    sb.append("    qualificationIds: ").append(toIndentedString(qualificationIds)).append("\n");
    sb.append("    experienceIds: ").append(toIndentedString(experienceIds)).append("\n");
    sb.append("    picId: ").append(toIndentedString(picId)).append("\n");
    sb.append("    joinDate: ").append(toIndentedString(joinDate)).append("\n");
    sb.append("    interviews: ").append(toIndentedString(interviews)).append("\n");
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
