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
 * 社内ユーザー一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientUserAccountItemsDto", description = "社内ユーザー一覧 ")
public class ClientUserAccountItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  
  private List<String> branches;

  
  private List<String> stores;

  @NotNull(message = "この項目は必須です。")
  private Long totalApplications = 0l;

  @NotNull(message = "この項目は必須です。")
  private Long totalNotSupportedApplications = 0l;

  private Boolean isExpired;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientUserAccountItemsDto clientUserAccountItemsDto = (ClientUserAccountItemsDto) o;
    return Objects.equals(this.id, clientUserAccountItemsDto.id) &&
        Objects.equals(this.fullName, clientUserAccountItemsDto.fullName) &&
        Objects.equals(this.email, clientUserAccountItemsDto.email) &&
        Objects.equals(this.branches, clientUserAccountItemsDto.branches) &&
        Objects.equals(this.stores, clientUserAccountItemsDto.stores) &&
        Objects.equals(this.totalApplications, clientUserAccountItemsDto.totalApplications) &&
        Objects.equals(this.totalNotSupportedApplications, clientUserAccountItemsDto.totalNotSupportedApplications) &&
        Objects.equals(this.isExpired, clientUserAccountItemsDto.isExpired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, email, branches, stores, totalApplications, totalNotSupportedApplications, isExpired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientUserAccountItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
    sb.append("    totalApplications: ").append(toIndentedString(totalApplications)).append("\n");
    sb.append("    totalNotSupportedApplications: ").append(toIndentedString(totalNotSupportedApplications)).append("\n");
    sb.append("    isExpired: ").append(toIndentedString(isExpired)).append("\n");
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
