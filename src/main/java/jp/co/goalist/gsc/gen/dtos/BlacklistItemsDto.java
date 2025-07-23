package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 選考対象外一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "BlacklistItemsDto", description = "選考対象外一覧 ")
public class BlacklistItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime createdDateTime;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String birthday = null;

  private String tel = null;

  private String email = null;

  private String memo = null;

  private Boolean isRestricted;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlacklistItemsDto blacklistItemsDto = (BlacklistItemsDto) o;
    return Objects.equals(this.id, blacklistItemsDto.id) &&
        Objects.equals(this.createdDateTime, blacklistItemsDto.createdDateTime) &&
        Objects.equals(this.fullName, blacklistItemsDto.fullName) &&
        Objects.equals(this.birthday, blacklistItemsDto.birthday) &&
        Objects.equals(this.tel, blacklistItemsDto.tel) &&
        Objects.equals(this.email, blacklistItemsDto.email) &&
        Objects.equals(this.memo, blacklistItemsDto.memo) &&
        Objects.equals(this.isRestricted, blacklistItemsDto.isRestricted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdDateTime, fullName, birthday, tel, email, memo, isRestricted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlacklistItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    createdDateTime: ").append(toIndentedString(createdDateTime)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    isRestricted: ").append(toIndentedString(isRestricted)).append("\n");
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
