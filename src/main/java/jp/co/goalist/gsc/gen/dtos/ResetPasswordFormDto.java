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
 * Reset Password Form
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "ResetPasswordFormDto", description = "Reset Password Form")
public class ResetPasswordFormDto {

  @NotBlank(message = "この項目は必須です。")
  private String token;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.CustomPasswordConstraint
  private String password;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.CustomPasswordConstraint
  private String confirmPassword;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetPasswordFormDto resetPasswordFormDto = (ResetPasswordFormDto) o;
    return Objects.equals(this.token, resetPasswordFormDto.token) &&
        Objects.equals(this.password, resetPasswordFormDto.password) &&
        Objects.equals(this.confirmPassword, resetPasswordFormDto.confirmPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, password, confirmPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetPasswordFormDto {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    confirmPassword: ").append(toIndentedString(confirmPassword)).append("\n");
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
