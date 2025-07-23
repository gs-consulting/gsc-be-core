package jp.co.goalist.gsc.enums;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum ScreenPermission {

    MESSAGE("1", "メッセージ"),
    ADVERTISEMENT("2", "広告管理"),
    PROJECT("3", "案件管理"),
    FILE("4", "ファイル");

    private final String id;
    private final String name;

    @Getter
    private static final List<String> screenPermissions = List.of(
            MESSAGE.getId(),
            ADVERTISEMENT.getId(),
            PROJECT.getId(),
            FILE.getId());

    ScreenPermission(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static ScreenPermission fromId(String value) {
        for (ScreenPermission item : ScreenPermission.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "機能制限", value));
    }

}
