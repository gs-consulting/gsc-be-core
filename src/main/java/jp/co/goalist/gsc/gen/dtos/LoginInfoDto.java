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
 * Login response
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "LoginInfoDto", description = "Login response")
public class LoginInfoDto {

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String oemGroupName = null;

  @NotBlank(message = "この項目は必須です。")
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String accessToken;

  @NotBlank(message = "この項目は必須です。")
  private String role;

  
  private List<String> permissions;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginInfoDto loginInfoDto = (LoginInfoDto) o;
    return Objects.equals(this.fullName, loginInfoDto.fullName) &&
        Objects.equals(this.oemGroupName, loginInfoDto.oemGroupName) &&
        Objects.equals(this.email, loginInfoDto.email) &&
        Objects.equals(this.accessToken, loginInfoDto.accessToken) &&
        Objects.equals(this.role, loginInfoDto.role) &&
        Objects.equals(this.permissions, loginInfoDto.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, oemGroupName, email, accessToken, role, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginInfoDto {\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    oemGroupName: ").append(toIndentedString(oemGroupName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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
