package jp.co.goalist.gsc.enums;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum DateType {

    ADVERTISEMENT_DATE("ADVERTISEMENT_DATE", "広告日起算"),
    APPLICATION_DATE("APPLICATION_DATE", "計上日起算");

    private final String id;
    private final String name;

    DateType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static DateType fromId(String value) {
        for (DateType item : DateType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "日起算", value));
    }
}
