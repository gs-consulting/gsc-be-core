package jp.co.goalist.gsc.enums;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * 区分
 */
@Getter
public enum SalaryType {

    YEARLY("YEARLY", "年収"),
    MONTHLY("MONTHLY", "月給"),
    DAILY("DAILY", "日給"),
    HOURLY("HOURLY", "時給");
    
    SalaryType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;

    public static boolean checkExistingType(String value) {
        for (SalaryType item : SalaryType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return true;
            }
        }

        return false;
    }

    @JsonCreator
    public static SalaryType fromId(String value) {
        for (SalaryType item : SalaryType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "給与_区分", value));
    }
}
