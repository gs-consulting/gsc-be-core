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
 * クライアントのアカウント情報
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "ClientAccountItemsDto", description = "クライアントのアカウント情報")
public class ClientAccountItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String clientName;

  
  private List<String> managers;

  
  private List<String> branches;

  
  private List<String> stores;

  @NotNull(message = "この項目は必須です。")
  private Long totalApplications = 0l;

  @NotNull(message = "この項目は必須です。")
  private Long totalNotSupportedApplications = 0l;

  private Boolean isMember;

  private Boolean isExpired;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientAccountItemsDto clientAccountItemsDto = (ClientAccountItemsDto) o;
    return Objects.equals(this.id, clientAccountItemsDto.id) &&
        Objects.equals(this.clientName, clientAccountItemsDto.clientName) &&
        Objects.equals(this.managers, clientAccountItemsDto.managers) &&
        Objects.equals(this.branches, clientAccountItemsDto.branches) &&
        Objects.equals(this.stores, clientAccountItemsDto.stores) &&
        Objects.equals(this.totalApplications, clientAccountItemsDto.totalApplications) &&
        Objects.equals(this.totalNotSupportedApplications, clientAccountItemsDto.totalNotSupportedApplications) &&
        Objects.equals(this.isMember, clientAccountItemsDto.isMember) &&
        Objects.equals(this.isExpired, clientAccountItemsDto.isExpired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientName, managers, branches, stores, totalApplications, totalNotSupportedApplications, isMember, isExpired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientAccountItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
    sb.append("    managers: ").append(toIndentedString(managers)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
    sb.append("    totalApplications: ").append(toIndentedString(totalApplications)).append("\n");
    sb.append("    totalNotSupportedApplications: ").append(toIndentedString(totalNotSupportedApplications)).append("\n");
    sb.append("    isMember: ").append(toIndentedString(isMember)).append("\n");
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
