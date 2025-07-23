package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.ClientBranchRestrictionUpsertDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * クライアント・支店登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientBranchUpsertDto", description = "クライアント・支店登録 ")
public class ClientBranchUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
  private String furiganaName;

  private String branchCode = null;

  @jp.co.goalist.gsc.utils.validation.PostCodeConstraint
  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.FaxCodeConstraint
  private String faxCode = null;

  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email = null;

  private String memo = null;

  @NotBlank(message = "この項目は必須です。")
  private String staffPermission;

  @NotBlank(message = "この項目は必須です。")
  private String partTimePermission;

  @Valid
  private List<@Valid ClientBranchRestrictionUpsertDto> managerRestrictions;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientBranchUpsertDto clientBranchUpsertDto = (ClientBranchUpsertDto) o;
    return Objects.equals(this.name, clientBranchUpsertDto.name) &&
        Objects.equals(this.furiganaName, clientBranchUpsertDto.furiganaName) &&
        Objects.equals(this.branchCode, clientBranchUpsertDto.branchCode) &&
        Objects.equals(this.postCode, clientBranchUpsertDto.postCode) &&
        Objects.equals(this.prefecture, clientBranchUpsertDto.prefecture) &&
        Objects.equals(this.city, clientBranchUpsertDto.city) &&
        Objects.equals(this.tel, clientBranchUpsertDto.tel) &&
        Objects.equals(this.faxCode, clientBranchUpsertDto.faxCode) &&
        Objects.equals(this.email, clientBranchUpsertDto.email) &&
        Objects.equals(this.memo, clientBranchUpsertDto.memo) &&
        Objects.equals(this.staffPermission, clientBranchUpsertDto.staffPermission) &&
        Objects.equals(this.partTimePermission, clientBranchUpsertDto.partTimePermission) &&
        Objects.equals(this.managerRestrictions, clientBranchUpsertDto.managerRestrictions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, furiganaName, branchCode, postCode, prefecture, city, tel, faxCode, email, memo, staffPermission, partTimePermission, managerRestrictions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientBranchUpsertDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    branchCode: ").append(toIndentedString(branchCode)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    staffPermission: ").append(toIndentedString(staffPermission)).append("\n");
    sb.append("    partTimePermission: ").append(toIndentedString(partTimePermission)).append("\n");
    sb.append("    managerRestrictions: ").append(toIndentedString(managerRestrictions)).append("\n");
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
