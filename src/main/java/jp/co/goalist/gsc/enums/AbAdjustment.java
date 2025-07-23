package jp.co.goalist.gsc.enums;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum AbAdjustment {

    A("A", "A"),
    B("B", "B"),
    NONE("NONE", "未入力");

    AbAdjustment(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;

    public static boolean checkExistingTypeExcludeNone(String item) {
        return Arrays.stream(AbAdjustment.values())
                    .filter(ab -> ab != AbAdjustment.NONE)
                    .map(AbAdjustment::getId)
                    .anyMatch(id -> id.equals(item));
    }

    @JsonCreator
    public static AbAdjustment fromId(String id) {
        for (AbAdjustment item : AbAdjustment.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "AB判定", id));
    }
}
