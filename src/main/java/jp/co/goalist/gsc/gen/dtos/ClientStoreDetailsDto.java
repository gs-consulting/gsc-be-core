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
 * 拠点・店舗詳細 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientStoreDetailsDto", description = "拠点・店舗詳細 ")
public class ClientStoreDetailsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotBlank(message = "この項目は必須です。")
  private String furiganaName;

  @NotBlank(message = "この項目は必須です。")
  private String branchId;

  private String storeCode = null;

  private String postCode = null;

  private String prefecture = null;

  private String city = null;

  private String tel = null;

  private String faxCode = null;

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
    ClientStoreDetailsDto clientStoreDetailsDto = (ClientStoreDetailsDto) o;
    return Objects.equals(this.id, clientStoreDetailsDto.id) &&
        Objects.equals(this.name, clientStoreDetailsDto.name) &&
        Objects.equals(this.furiganaName, clientStoreDetailsDto.furiganaName) &&
        Objects.equals(this.branchId, clientStoreDetailsDto.branchId) &&
        Objects.equals(this.storeCode, clientStoreDetailsDto.storeCode) &&
        Objects.equals(this.postCode, clientStoreDetailsDto.postCode) &&
        Objects.equals(this.prefecture, clientStoreDetailsDto.prefecture) &&
        Objects.equals(this.city, clientStoreDetailsDto.city) &&
        Objects.equals(this.tel, clientStoreDetailsDto.tel) &&
        Objects.equals(this.faxCode, clientStoreDetailsDto.faxCode) &&
        Objects.equals(this.email, clientStoreDetailsDto.email) &&
        Objects.equals(this.memo, clientStoreDetailsDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, furiganaName, branchId, storeCode, postCode, prefecture, city, tel, faxCode, email, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientStoreDetailsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
