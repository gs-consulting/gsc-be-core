package jp.co.goalist.gsc.enums;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum DataType {

    INTERVIEWER("INTERVIEWER", "面接官別"),
    STORE("STORE", "拠点・店舗別"),
    OCCUPATION("OCCUPATION", "職種別"),
    MEDIA_NAME("MEDIA_NAME", "媒体別"),
    EDUCATION_OFFICER("EDUCATION_OFFICER", "初動教育担当別");

    private final String id;
    private final String name;

    DataType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static DataType fromId(String value) {
        for (DataType item : DataType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "データ種類", value));
    }
}
