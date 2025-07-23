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
 * CrawlerMediasDto
 */

@lombok.Getter
@lombok.Setter
public class CrawlerMediasDto {

  @NotBlank(message = "この項目は必須です。")
  private String mediaId;

  @NotBlank(message = "この項目は必須です。")
  private String loginId;

  @NotBlank(message = "この項目は必須です。")
  private String password;

  @NotBlank(message = "この項目は必須です。")
  private String parentId;

  @NotBlank(message = "この項目は必須です。")
  private String oemGroupId;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CrawlerMediasDto crawlerMediasDto = (CrawlerMediasDto) o;
    return Objects.equals(this.mediaId, crawlerMediasDto.mediaId) &&
        Objects.equals(this.loginId, crawlerMediasDto.loginId) &&
        Objects.equals(this.password, crawlerMediasDto.password) &&
        Objects.equals(this.parentId, crawlerMediasDto.parentId) &&
        Objects.equals(this.oemGroupId, crawlerMediasDto.oemGroupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaId, loginId, password, parentId, oemGroupId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CrawlerMediasDto {\n");
    sb.append("    mediaId: ").append(toIndentedString(mediaId)).append("\n");
    sb.append("    loginId: ").append(toIndentedString(loginId)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    oemGroupId: ").append(toIndentedString(oemGroupId)).append("\n");
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
