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
 * Login request (ログイン)
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "LoginFormDto", description = "Login request (ログイン)")
public class LoginFormDto {

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.CustomPasswordConstraint
  private String password;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginFormDto loginFormDto = (LoginFormDto) o;
    return Objects.equals(this.email, loginFormDto.email) &&
        Objects.equals(this.password, loginFormDto.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginFormDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
