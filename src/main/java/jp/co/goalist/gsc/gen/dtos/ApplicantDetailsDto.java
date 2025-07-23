package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.ApplicantInterviewDto;
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
@Schema(name = "ApplicantDetailsDto", description = "応募者情報 ")
public class ApplicantDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String furiganaName = null;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate birthday = null;

  private String gender = null;

  private String projectId = null;

  private String mediaId = null;

  private String email = null;

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

  @NotBlank(message = "この項目は必須です。")
  private String manuscriptUrl;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate joinDate = null;

  @Valid
  private List<@Valid ApplicantInterviewDto> interviews = new ArrayList<>();

  private String picId = null;

  private Boolean isCrawledData;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicantDetailsDto applicantDetailsDto = (ApplicantDetailsDto) o;
    return Objects.equals(this.id, applicantDetailsDto.id) &&
        Objects.equals(this.fullName, applicantDetailsDto.fullName) &&
        Objects.equals(this.furiganaName, applicantDetailsDto.furiganaName) &&
        Objects.equals(this.birthday, applicantDetailsDto.birthday) &&
        Objects.equals(this.gender, applicantDetailsDto.gender) &&
        Objects.equals(this.projectId, applicantDetailsDto.projectId) &&
        Objects.equals(this.mediaId, applicantDetailsDto.mediaId) &&
        Objects.equals(this.email, applicantDetailsDto.email) &&
        Objects.equals(this.tel, applicantDetailsDto.tel) &&
        Objects.equals(this.postCode, applicantDetailsDto.postCode) &&
        Objects.equals(this.prefecture, applicantDetailsDto.prefecture) &&
        Objects.equals(this.city, applicantDetailsDto.city) &&
        Objects.equals(this.homeAddress, applicantDetailsDto.homeAddress) &&
        Objects.equals(this.occupation, applicantDetailsDto.occupation) &&
        Objects.equals(this.selectionStatusId, applicantDetailsDto.selectionStatusId) &&
        Objects.equals(this.qualificationIds, applicantDetailsDto.qualificationIds) &&
        Objects.equals(this.experienceIds, applicantDetailsDto.experienceIds) &&
        Objects.equals(this.manuscriptUrl, applicantDetailsDto.manuscriptUrl) &&
        Objects.equals(this.joinDate, applicantDetailsDto.joinDate) &&
        Objects.equals(this.interviews, applicantDetailsDto.interviews) &&
        Objects.equals(this.picId, applicantDetailsDto.picId) &&
        Objects.equals(this.isCrawledData, applicantDetailsDto.isCrawledData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, furiganaName, birthday, gender, projectId, mediaId, email, tel, postCode, prefecture, city, homeAddress, occupation, selectionStatusId, qualificationIds, experienceIds, manuscriptUrl, joinDate, interviews, picId, isCrawledData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicantDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
    sb.append("    manuscriptUrl: ").append(toIndentedString(manuscriptUrl)).append("\n");
    sb.append("    joinDate: ").append(toIndentedString(joinDate)).append("\n");
    sb.append("    interviews: ").append(toIndentedString(interviews)).append("\n");
    sb.append("    picId: ").append(toIndentedString(picId)).append("\n");
    sb.append("    isCrawledData: ").append(toIndentedString(isCrawledData)).append("\n");
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
