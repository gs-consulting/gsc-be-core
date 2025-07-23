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
 * 拠点・店舗一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientStoreItemsDto", description = "拠点・店舗一覧 ")
public class ClientStoreItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  private String branch = null;

  private String address = null;

  private String tel = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientStoreItemsDto clientStoreItemsDto = (ClientStoreItemsDto) o;
    return Objects.equals(this.id, clientStoreItemsDto.id) &&
        Objects.equals(this.name, clientStoreItemsDto.name) &&
        Objects.equals(this.branch, clientStoreItemsDto.branch) &&
        Objects.equals(this.address, clientStoreItemsDto.address) &&
        Objects.equals(this.tel, clientStoreItemsDto.tel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, branch, address, tel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientStoreItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
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
