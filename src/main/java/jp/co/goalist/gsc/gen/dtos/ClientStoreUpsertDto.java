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
 * クライアント・拠点・店舗登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientStoreUpsertDto", description = "クライアント・拠点・店舗登録 ")
public class ClientStoreUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
  private String furiganaName;

  @NotBlank(message = "この項目は必須です。")
  private String branchId;

  private String storeCode = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientStoreUpsertDto clientStoreUpsertDto = (ClientStoreUpsertDto) o;
    return Objects.equals(this.name, clientStoreUpsertDto.name) &&
        Objects.equals(this.furiganaName, clientStoreUpsertDto.furiganaName) &&
        Objects.equals(this.branchId, clientStoreUpsertDto.branchId) &&
        Objects.equals(this.storeCode, clientStoreUpsertDto.storeCode) &&
        Objects.equals(this.postCode, clientStoreUpsertDto.postCode) &&
        Objects.equals(this.prefecture, clientStoreUpsertDto.prefecture) &&
        Objects.equals(this.city, clientStoreUpsertDto.city) &&
        Objects.equals(this.tel, clientStoreUpsertDto.tel) &&
        Objects.equals(this.faxCode, clientStoreUpsertDto.faxCode) &&
        Objects.equals(this.email, clientStoreUpsertDto.email) &&
        Objects.equals(this.memo, clientStoreUpsertDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, furiganaName, branchId, storeCode, postCode, prefecture, city, tel, faxCode, email, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientStoreUpsertDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    storeCode: ").append(toIndentedString(storeCode)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("    prefecture: ").append(toIndentedString(prefecture)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
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
