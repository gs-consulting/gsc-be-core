package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ApplicantStatusType {

    VALID("VALID", "有効"),
    DUPLICATE("DUPLICATE", "重複"),
    BLACKLIST("BLACKLIST", "対象外");

    private final String id;
    private final String name;

    ApplicantStatusType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static ApplicantStatusType fromId(String value) {
        for (ApplicantStatusType item : ApplicantStatusType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "経験の有無", value));
    }
}