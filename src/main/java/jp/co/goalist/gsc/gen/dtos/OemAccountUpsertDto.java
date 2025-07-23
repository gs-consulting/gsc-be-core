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
 * OEMアカウント登録
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "OemAccountUpsertDto", description = "OEMアカウント登録")
public class OemAccountUpsertDto {

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.EmailConstraint
  private String email;

  @NotBlank(message = "この項目は必須です。")
  private String fullName;

  @NotBlank(message = "この項目は必須です。")
  @jp.co.goalist.gsc.utils.validation.FuriganaConstraint
  private String furiganaName;

  
  private List<String> teamIds;

  @jp.co.goalist.gsc.utils.validation.PhoneNumberConstraint
  private String tel = null;

  @jp.co.goalist.gsc.utils.validation.FaxCodeConstraint
  private String faxCode = null;

  private String memo = null;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OemAccountUpsertDto oemAccountUpsertDto = (OemAccountUpsertDto) o;
    return Objects.equals(this.email, oemAccountUpsertDto.email) &&
        Objects.equals(this.fullName, oemAccountUpsertDto.fullName) &&
        Objects.equals(this.furiganaName, oemAccountUpsertDto.furiganaName) &&
        Objects.equals(this.teamIds, oemAccountUpsertDto.teamIds) &&
        Objects.equals(this.tel, oemAccountUpsertDto.tel) &&
        Objects.equals(this.faxCode, oemAccountUpsertDto.faxCode) &&
        Objects.equals(this.memo, oemAccountUpsertDto.memo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, fullName, furiganaName, teamIds, tel, faxCode, memo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OemAccountUpsertDto {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    furiganaName: ").append(toIndentedString(furiganaName)).append("\n");
    sb.append("    teamIds: ").append(toIndentedString(teamIds)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    faxCode: ").append(toIndentedString(faxCode)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
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
