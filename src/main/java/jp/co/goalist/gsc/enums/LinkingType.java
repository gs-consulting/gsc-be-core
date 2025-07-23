package jp.co.goalist.gsc.enums;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * 案件の紐付け方
 */
@Getter
public enum LinkingType {

    SPECIFIC("SPECIFIC", "案件を指定して紐付け"),
    ALL("ALL", "全案件に紐付け");

    LinkingType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;

    @JsonCreator
    public static LinkingType fromId(String value) {
        for (LinkingType item : LinkingType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "案件の紐付け方", value));
    }
}
