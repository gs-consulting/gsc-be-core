package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum PublishingStatus {

    PUBLIC("PUBLIC", "公開"),
    PRIVATE("PRIVATE", "非公開");

    private final String id;
    private final String name;

    PublishingStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static PublishingStatus fromId(String value) {
        for (PublishingStatus item : PublishingStatus.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "ステータス", value));
    }
}
