package jp.co.goalist.gsc.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public class ZipCodeResponse {

    @JsonProperty("message")
    String message;

    @JsonProperty("results")
    List<ZipCode> results;

    @JsonProperty("status")
    String status;
}
