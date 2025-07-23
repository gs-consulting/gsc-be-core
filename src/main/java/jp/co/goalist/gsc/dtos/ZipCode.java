package jp.co.goalist.gsc.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZipCode {

    @JsonProperty("address1")
    String address1;

    @JsonProperty("address2")
    String address2;

    @JsonProperty("address3")
    String address3;

    @JsonProperty("kana1")
    String kana1;

    @JsonProperty("kana2")
    String kana2;

    @JsonProperty("kana3")
    String kana3;

    @JsonProperty("prefcode")
    String prefcode;

    @JsonProperty("zipcode")
    String zipcode;
}
