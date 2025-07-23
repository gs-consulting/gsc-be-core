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
 * 未読メッセージ
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "DChatItemsDto", description = "未読メッセージ")
public class DChatItemsDto {

  @NotBlank(message = "この項目は必須です。")
  private String id;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  private String gender = null;

  private Integer age = null;

  private String address = null;

  @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
  private LocalDateTime repliedDate;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DChatItemsDto dchatItemsDto = (DChatItemsDto) o;
    return Objects.equals(this.id, dchatItemsDto.id) &&
        Objects.equals(this.fullName, dchatItemsDto.fullName) &&
        Objects.equals(this.gender, dchatItemsDto.gender) &&
        Objects.equals(this.age, dchatItemsDto.age) &&
        Objects.equals(this.address, dchatItemsDto.address) &&
        Objects.equals(this.repliedDate, dchatItemsDto.repliedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, gender, age, address, repliedDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DChatItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    age: ").append(toIndentedString(age)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    repliedDate: ").append(toIndentedString(repliedDate)).append("\n");
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
