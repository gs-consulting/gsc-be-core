package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * ExternalApplicantCreateDto
 */

@lombok.Getter
@lombok.Setter
public class ExternalApplicantCreateDto {

  @NotBlank(message = "この項目は必須です。")
  private String userId;

  private String mediaName = null;

  private String manuscriptUrl = null;

  private String occupation = null;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String furiganaName = null;

  private String gender = null;

  private String birthday = null;

  private String homeAddress = null;

  private String email = null;

  private String tel = null;

  private String memo = null;

  private String applicationDate = null;

  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  private String kyujinApplicantId = null;

  private String kyujinJobId = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalApplicantCreateDto externalApplicantCreateDto = (ExternalApplicantCreateDto) o;
    return Objects.equals(this.userId, externalApplicantCreateDto.userId) &&
        Objects.equals(this.mediaName, externalApplicantCreateDto.mediaName) &&
        Objects.equals(this.manuscriptUrl, externalApplicantCreateDto.manuscriptUrl) &&
        Objects.equals(this.occupation, externalApplicantCreateDto.occupation) &&
        Objects.equals(this.fullName, externalApplicantCreateDto.fullName) &&
        Objects.equals(this.furiganaName, externalApplicantCreateDto.furiganaName) &&
        Objects.equals(this.gender, externalApplicantCreateDto.gender) &&
        Objects.equals(this.birthday, externalApplicantCreateDto.birthday) &&
        Objects.equals(this.homeAddress, externalApplicantCreateDto.homeAddress) &&
        Objects.equals(this.email, externalApplicantCreateDto.email) &&
        Objects.equals(this.tel, externalApplicantCreateDto.tel) &&
        Objects.equals(this.memo, externalApplicantCreateDto.memo) &&
        Objects.equals(this.applicationDate, externalApplicantCreateDto.applicationDate) &&
        Objects.equals(this.postCode, externalApplicantCreateDto.postCode) &&
        Objects.equals(this.prefecture, externalApplicantCreateDto.prefecture) &&
        Objects.equals(this.city, externalApplicantCreateDto.city) &&
        Objects.equals(this.kyujinApplicantId, externalApplicantCreateDto.kyujinApplicantId) &&
        Objects.equals(this.kyujinJobId, externalApplicantCreateDto.kyujinJobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, mediaName, manuscriptUrl, occupation, fullName, furiganaName, gender, birthday, homeAddress, email, tel, memo, applicationDate, postCode, prefecture, city, kyujinApplicantId, kyujinJobId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalApplicantCreateDto {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    mediaName: ").append(toIndentedString(mediaName)).append("\n");
    sb.append("    manuscriptUrl: ").append(toIndentedString(manuscriptUrl)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    applicationDate: ").append(toIndentedString(applicationDate)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    kyujinApplicantId: ").append(toIndentedString(kyujinApplicantId)).append("\n");
    sb.append("    kyujinJobId: ").append(toIndentedString(kyujinJobId)).append("\n");
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
