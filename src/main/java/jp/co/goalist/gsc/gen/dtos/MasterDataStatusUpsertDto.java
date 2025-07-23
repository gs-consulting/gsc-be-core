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
 * MasterDataStatusUpsertDto
 */

@lombok.Getter
@lombok.Setter
public class MasterDataStatusUpsertDto {

  private String id = null;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @NotNull(message = "この項目は必須です。")
  private Integer order;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MasterDataStatusUpsertDto masterDataStatusUpsertDto = (MasterDataStatusUpsertDto) o;
    return Objects.equals(this.id, masterDataStatusUpsertDto.id) &&
        Objects.equals(this.name, masterDataStatusUpsertDto.name) &&
        Objects.equals(this.order, masterDataStatusUpsertDto.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MasterDataStatusUpsertDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
