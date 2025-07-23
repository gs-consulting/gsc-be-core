package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum ExperienceStatus {

    EXPERIENCED("EXPERIENCED", "経験者"),
    INEXPERIENCED("INEXPERIENCED", "未経験"),
    BOTH("BOTH", "どちらでも"),
    NONE("NONE", "未入力");

    private final String id;
    private final String name;

    ExperienceStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static boolean checkExistingTypeExcludeNone(String item) {
        return Arrays.stream(ExperienceStatus.values())
                    .filter(experience -> experience != ExperienceStatus.NONE)
                    .map(ExperienceStatus::getId)
                    .anyMatch(id -> id.equals(item));
    }

    @JsonCreator
    public static ExperienceStatus fromId(String value) {
        for (ExperienceStatus item : ExperienceStatus.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "経験の有無", value));
    }
}
