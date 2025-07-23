package jp.co.goalist.gsc.gen.dtos;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.goalist.gsc.gen.dtos.MTCityItemsDto;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 都道府県一覧 
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "MTPrefectureItemsDto", description = "都道府県一覧 ")
public class MTPrefectureItemsDto {

  @NotNull(message = "この項目は必須です。")
  private Integer id;

  @NotBlank(message = "この項目は必須です。")
  private String name;

  @Valid
  private List<@Valid MTCityItemsDto> cities = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MTPrefectureItemsDto mtPrefectureItemsDto = (MTPrefectureItemsDto) o;
    return Objects.equals(this.id, mtPrefectureItemsDto.id) &&
        Objects.equals(this.name, mtPrefectureItemsDto.name) &&
        Objects.equals(this.cities, mtPrefectureItemsDto.cities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, cities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MTPrefectureItemsDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    cities: ").append(toIndentedString(cities)).append("\n");
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
