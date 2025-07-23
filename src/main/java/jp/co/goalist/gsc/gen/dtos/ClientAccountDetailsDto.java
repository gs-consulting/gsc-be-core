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
 * クライアントのアカウント詳細
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientAccountDetailsDto", description = "クライアントのアカウント詳細")
public class ClientAccountDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String clientName;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  private String oemGroupId = null;

  
  private List<String> branchIds;

  
  private List<String> storeIds;

  
  private List<String> managerIds;

  private String clientCode = null;

  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  private String tel = null;

  private String faxCode = null;

  private String memo = null;

  private String domainSetting = null;

  private Boolean isDomainEnabled = false;

  private Boolean isCreatedByOperator;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientAccountDetailsDto clientAccountDetailsDto = (ClientAccountDetailsDto) o;
    return Objects.equals(this.id, clientAccountDetailsDto.id) &&
        Objects.equals(this.clientName, clientAccountDetailsDto.clientName) &&
        Objects.equals(this.furiganaName, clientAccountDetailsDto.furiganaName) &&
        Objects.equals(this.oemGroupId, clientAccountDetailsDto.oemGroupId) &&
        Objects.equals(this.branchIds, clientAccountDetailsDto.branchIds) &&
        Objects.equals(this.storeIds, clientAccountDetailsDto.storeIds) &&
        Objects.equals(this.managerIds, clientAccountDetailsDto.managerIds) &&
        Objects.equals(this.clientCode, clientAccountDetailsDto.clientCode) &&
        Objects.equals(this.postCode, clientAccountDetailsDto.postCode) &&
        Objects.equals(this.prefecture, clientAccountDetailsDto.prefecture) &&
        Objects.equals(this.city, clientAccountDetailsDto.city) &&
        Objects.equals(this.tel, clientAccountDetailsDto.tel) &&
        Objects.equals(this.faxCode, clientAccountDetailsDto.faxCode) &&
        Objects.equals(this.memo, clientAccountDetailsDto.memo) &&
        Objects.equals(this.domainSetting, clientAccountDetailsDto.domainSetting) &&
        Objects.equals(this.isDomainEnabled, clientAccountDetailsDto.isDomainEnabled) &&
        Objects.equals(this.isCreatedByOperator, clientAccountDetailsDto.isCreatedByOperator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientName, furiganaName, oemGroupId, branchIds, storeIds, managerIds, clientCode, postCode, prefecture, city, tel, faxCode, memo, domainSetting, isDomainEnabled, isCreatedByOperator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientAccountDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
    sb.append("    isDomainEnabled: ").append(toIndentedString(isDomainEnabled)).append("\n");
    sb.append("    isCreatedByOperator: ").append(toIndentedString(isCreatedByOperator)).append("\n");
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
