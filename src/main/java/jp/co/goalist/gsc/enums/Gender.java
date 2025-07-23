package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum Gender {
    MALE("MALE", "男"),
    FEMALE("FEMALE", "女"),
    BOTH("BOTH", "制限なし");

    private final String id;
    private final String name;

    Gender(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static Gender fromId(String value) {
        for (Gender item : Gender.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "性別", value));
    }

    public static boolean checkValidId(String item) {
        List<String> ids = Arrays.stream(Gender.values()).map(Gender::getId).toList();
        return ids.contains(item);
    }

    public static boolean checkValidIdExcludeBoth(String item) {
        return Arrays.stream(Gender.values())
                    .filter(gender -> gender != Gender.BOTH)
                    .map(Gender::getId)
                    .anyMatch(id -> id.equals(item));
    }
}
