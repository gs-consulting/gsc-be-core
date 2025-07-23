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
 * 支店詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientBranchDetailsDto", description = "支店詳細 ")
public class ClientBranchDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  private String branchCode = null;

  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  private String tel = null;

  private String faxCode = null;

  private String email = null;

  private String memo = null;

  @NotBlank(message = "この項目は必須です。")
  private String staffPermission;

  @NotBlank(message = "この項目は必須です。")
  private String partTimePermission;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientBranchDetailsDto clientBranchDetailsDto = (ClientBranchDetailsDto) o;
    return Objects.equals(this.id, clientBranchDetailsDto.id) &&
        Objects.equals(this.name, clientBranchDetailsDto.name) &&
        Objects.equals(this.furiganaName, clientBranchDetailsDto.furiganaName) &&
        Objects.equals(this.branchCode, clientBranchDetailsDto.branchCode) &&
        Objects.equals(this.postCode, clientBranchDetailsDto.postCode) &&
        Objects.equals(this.prefecture, clientBranchDetailsDto.prefecture) &&
        Objects.equals(this.city, clientBranchDetailsDto.city) &&
        Objects.equals(this.tel, clientBranchDetailsDto.tel) &&
        Objects.equals(this.faxCode, clientBranchDetailsDto.faxCode) &&
        Objects.equals(this.email, clientBranchDetailsDto.email) &&
        Objects.equals(this.memo, clientBranchDetailsDto.memo) &&
        Objects.equals(this.staffPermission, clientBranchDetailsDto.staffPermission) &&
        Objects.equals(this.partTimePermission, clientBranchDetailsDto.partTimePermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, furiganaName, branchCode, postCode, prefecture, city, tel, faxCode, email, memo, staffPermission, partTimePermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientBranchDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
