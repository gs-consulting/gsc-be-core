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
 * クライアントのアンケント登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientAccountUpsertDto", description = "クライアントのアンケント登録 ")
public class ClientAccountUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String clientName;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  private String oemGroupId = null;

  
  private List<String> branchIds;

  
  private List<String> storeIds;

  
  private List<String> managerIds;

  private String clientCode = null;

  @jp.co.goalist.gsc.utils.validation.PostCodeConstraint
  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.FaxCodeConstraint
  private String faxCode = null;

  private String memo = null;

  private String domainSetting = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientAccountUpsertDto clientAccountUpsertDto = (ClientAccountUpsertDto) o;
    return Objects.equals(this.clientName, clientAccountUpsertDto.clientName) &&
        Objects.equals(this.furiganaName, clientAccountUpsertDto.furiganaName) &&
        Objects.equals(this.oemGroupId, clientAccountUpsertDto.oemGroupId) &&
        Objects.equals(this.branchIds, clientAccountUpsertDto.branchIds) &&
        Objects.equals(this.storeIds, clientAccountUpsertDto.storeIds) &&
        Objects.equals(this.managerIds, clientAccountUpsertDto.managerIds) &&
        Objects.equals(this.clientCode, clientAccountUpsertDto.clientCode) &&
        Objects.equals(this.postCode, clientAccountUpsertDto.postCode) &&
        Objects.equals(this.prefecture, clientAccountUpsertDto.prefecture) &&
        Objects.equals(this.city, clientAccountUpsertDto.city) &&
        Objects.equals(this.tel, clientAccountUpsertDto.tel) &&
        Objects.equals(this.faxCode, clientAccountUpsertDto.faxCode) &&
        Objects.equals(this.memo, clientAccountUpsertDto.memo) &&
        Objects.equals(this.domainSetting, clientAccountUpsertDto.domainSetting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientName, furiganaName, oemGroupId, branchIds, storeIds, managerIds, clientCode, postCode, prefecture, city, tel, faxCode, memo, domainSetting);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientAccountUpsertDto {\n");
    sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    oemGroupId: ").append(toIndentedString(oemGroupId)).append("\n");
    sb.append("    branchIds: ").append(toIndentedString(branchIds)).append("\n");
    sb.append("    storeIds: ").append(toIndentedString(storeIds)).append("\n");
    sb.append("    managerIds: ").append(toIndentedString(managerIds)).append("\n");
    sb.append("    clientCode: ").append(toIndentedString(clientCode)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    domainSetting: ").append(toIndentedString(domainSetting)).append("\n");
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
