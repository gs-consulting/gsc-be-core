package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum EmploymentType {

    STAFF("STAFF", "社員"),
    PART("PART", "アルバイト");

    private final String id;
    private final String name;

    EmploymentType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static EmploymentType fromId(String value) {
        for (EmploymentType item : EmploymentType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "雇用形態", value));
    }
}
