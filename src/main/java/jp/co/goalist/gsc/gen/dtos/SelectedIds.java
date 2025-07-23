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
 * 削除されるIDs
 */

@lombok.Getter
@lombok.Setter
@Schema(name = "SelectedIds", description = "削除されるIDs")
public class SelectedIds {

  
  private List<String> selectedIds = new ArrayList<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SelectedIds selectedIds = (SelectedIds) o;
    return Objects.equals(this.selectedIds, selectedIds.selectedIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(selectedIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectedIds {\n");
    sb.append("    selectedIds: ").append(toIndentedString(selectedIds)).append("\n");
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
