package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * 職種、雇用形態、勤務期間、面接会場、資格
 */
@Getter
public enum StatusType {

    PROJECT_STATUS("PROJECT_STATUS", "ステータス"),
    OCCUPATION("OCCUPATION", "職種"),
    EMPLOYMENT_TYPE("EMPLOYMENT_TYPE", "雇用形態"),
    WORKING_PERIOD("WORKING_PERIOD", "勤務期間"),
    INTERVIEW_LOC("INTERVIEW_LOC", "面接会場"),
    QUALIFICATION("QUALIFICATION", "資格"),
    EXPERIENCE("EXPERIENCE", "経験");

    StatusType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;

    @JsonCreator
    public static StatusType fromId(String id) {
        for (StatusType statusType : StatusType.values()) {
            if (statusType.getId().equals(id)) {
                return statusType;
            }
        }
        
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "案件ステータス", id));
    }
}
