package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum QuestionType {
    SINGLE("SINGLE", "選択方式"),
    MULTIPLE("MULTIPLE", "複数選択方式"),
    FREE_TEXT("FREE_TEXT", "自由記述方式");

    private final String id;
    private final String name;

    QuestionType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static QuestionType fromId(String id) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.getId().equals(id)) {
                return questionType;
            }
        }
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "選択方式", id));
    }
}
