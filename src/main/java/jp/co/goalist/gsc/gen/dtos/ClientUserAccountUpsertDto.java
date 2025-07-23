package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 社内ユーザー情報 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientUserAccountUpsertDto", description = "社内ユーザー情報 ")
public class ClientUserAccountUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
  private String furiganaName;

  
  private List<String> branchIds;

  
  private List<String> storeIds;

  private String clientCode = null;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.FaxCodeConstraint
  private String faxCode = null;

  private String memo = null;

  @NotBlank(message = "この項目は必須です。")
  private String employmentType;

  private Boolean isInterviewer = false;

  private Boolean isEducationOfficer = false;

  
  private List<String> permissions;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientUserAccountUpsertDto clientUserAccountUpsertDto = (ClientUserAccountUpsertDto) o;
    return Objects.equals(this.email, clientUserAccountUpsertDto.email) &&
        Objects.equals(this.fullName, clientUserAccountUpsertDto.fullName) &&
        Objects.equals(this.furiganaName, clientUserAccountUpsertDto.furiganaName) &&
        Objects.equals(this.branchIds, clientUserAccountUpsertDto.branchIds) &&
        Objects.equals(this.storeIds, clientUserAccountUpsertDto.storeIds) &&
        Objects.equals(this.clientCode, clientUserAccountUpsertDto.clientCode) &&
        Objects.equals(this.tel, clientUserAccountUpsertDto.tel) &&
        Objects.equals(this.faxCode, clientUserAccountUpsertDto.faxCode) &&
        Objects.equals(this.memo, clientUserAccountUpsertDto.memo) &&
        Objects.equals(this.employmentType, clientUserAccountUpsertDto.employmentType) &&
        Objects.equals(this.isInterviewer, clientUserAccountUpsertDto.isInterviewer) &&
        Objects.equals(this.isEducationOfficer, clientUserAccountUpsertDto.isEducationOfficer) &&
        Objects.equals(this.permissions, clientUserAccountUpsertDto.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, fullName, furiganaName, branchIds, storeIds, clientCode, tel, faxCode, memo, employmentType, isInterviewer, isEducationOfficer, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientUserAccountUpsertDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    branchIds: ").append(toIndentedString(branchIds)).append("\n");
    sb.append("    storeIds: ").append(toIndentedString(storeIds)).append("\n");
    sb.append("    clientCode: ").append(toIndentedString(clientCode)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    employmentType: ").append(toIndentedString(employmentType)).append("\n");
    sb.append("    isInterviewer: ").append(toIndentedString(isInterviewer)).append("\n");
    sb.append("    isEducationOfficer: ").append(toIndentedString(isEducationOfficer)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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
