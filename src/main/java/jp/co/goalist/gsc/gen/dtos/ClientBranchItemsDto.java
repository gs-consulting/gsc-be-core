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
 * 支店一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientBranchItemsDto", description = "支店一覧 ")
public class ClientBranchItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  
  private List<String> storeNames = new ArrayList<>();

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
    ClientBranchItemsDto clientBranchItemsDto = (ClientBranchItemsDto) o;
    return Objects.equals(this.id, clientBranchItemsDto.id) &&
        Objects.equals(this.name, clientBranchItemsDto.name) &&
        Objects.equals(this.storeNames, clientBranchItemsDto.storeNames) &&
        Objects.equals(this.address, clientBranchItemsDto.address) &&
        Objects.equals(this.tel, clientBranchItemsDto.tel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, storeNames, address, tel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientBranchItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    storeNames: ").append(toIndentedString(storeNames)).append("\n");
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
