package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 選考対象外一覧登録 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "BlacklistCreateDto", description = "選考対象外一覧登録 ")
public class BlacklistCreateDto {

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @JsonFormat(pattern = "yyyy/MM/dd")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate birthday = null;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlacklistCreateDto blacklistCreateDto = (BlacklistCreateDto) o;
    return Objects.equals(this.fullName, blacklistCreateDto.fullName) &&
        Objects.equals(this.birthday, blacklistCreateDto.birthday) &&
        Objects.equals(this.tel, blacklistCreateDto.tel) &&
        Objects.equals(this.email, blacklistCreateDto.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, birthday, tel, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlacklistCreateDto {\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
