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
 * データレスポンス 
 */

@lombok.Getter
@lombok.Setter
@lombok.Builder
@Schema(name = "ErrorResponse", description = "データレスポンス ")
public class ErrorResponse {

  @NotBlank(message = "この項目は必須です。")
  private String statusCode;

  @NotBlank(message = "この項目は必須です。")
  private String message;

  private String fieldError = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this.statusCode, errorResponse.statusCode) &&
        Objects.equals(this.message, errorResponse.message) &&
        Objects.equals(this.fieldError, errorResponse.fieldError);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, message, fieldError);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    fieldError: ").append(toIndentedString(fieldError)).append("\n");
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
