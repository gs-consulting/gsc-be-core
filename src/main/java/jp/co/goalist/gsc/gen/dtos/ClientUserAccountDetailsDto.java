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
 * 社内ユーザー登録
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientUserAccountDetailsDto", description = "社内ユーザー登録")
public class ClientUserAccountDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  
  private List<String> branchIds;

  
  private List<String> storeIds;

  private String clientCode = null;

  private String tel = null;

  private String faxCode = null;

  private String memo = null;

  private String employmentType = null;

  private Boolean isInterviewer;

  private Boolean isEducationOfficer;

  
  private List<String> permissions;

  private Boolean isRestricted = false;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientUserAccountDetailsDto clientUserAccountDetailsDto = (ClientUserAccountDetailsDto) o;
    return Objects.equals(this.id, clientUserAccountDetailsDto.id) &&
        Objects.equals(this.email, clientUserAccountDetailsDto.email) &&
        Objects.equals(this.fullName, clientUserAccountDetailsDto.fullName) &&
        Objects.equals(this.furiganaName, clientUserAccountDetailsDto.furiganaName) &&
        Objects.equals(this.branchIds, clientUserAccountDetailsDto.branchIds) &&
        Objects.equals(this.storeIds, clientUserAccountDetailsDto.storeIds) &&
        Objects.equals(this.clientCode, clientUserAccountDetailsDto.clientCode) &&
        Objects.equals(this.tel, clientUserAccountDetailsDto.tel) &&
        Objects.equals(this.faxCode, clientUserAccountDetailsDto.faxCode) &&
        Objects.equals(this.memo, clientUserAccountDetailsDto.memo) &&
        Objects.equals(this.employmentType, clientUserAccountDetailsDto.employmentType) &&
        Objects.equals(this.isInterviewer, clientUserAccountDetailsDto.isInterviewer) &&
        Objects.equals(this.isEducationOfficer, clientUserAccountDetailsDto.isEducationOfficer) &&
        Objects.equals(this.permissions, clientUserAccountDetailsDto.permissions) &&
        Objects.equals(this.isRestricted, clientUserAccountDetailsDto.isRestricted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, fullName, furiganaName, branchIds, storeIds, clientCode, tel, faxCode, memo, employmentType, isInterviewer, isEducationOfficer, permissions, isRestricted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientUserAccountDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
    sb.append("    isRestricted: ").append(toIndentedString(isRestricted)).append("\n");
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
