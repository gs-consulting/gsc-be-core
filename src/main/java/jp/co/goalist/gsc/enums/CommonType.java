package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum CommonType {
    NONE("NONE", "なし");

    private final String id;
    private final String name;

    CommonType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static CommonType fromId(String value) {
        for (CommonType item : CommonType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "経験の有無", value));
    }
}