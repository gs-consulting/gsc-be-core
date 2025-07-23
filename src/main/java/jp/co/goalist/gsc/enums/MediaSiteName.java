package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum MediaSiteName {

    MYNAVI("MYNAVI", "マイナビ");

    private final String id;
    private final String name;

    MediaSiteName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static MediaSiteName fromId(String value) {
        for (MediaSiteName item : MediaSiteName.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "連携先 管理画面", value));
    }
}
