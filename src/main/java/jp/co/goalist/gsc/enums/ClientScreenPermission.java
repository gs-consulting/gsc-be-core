package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public enum ClientScreenPermission {

    VIEW("VIEW", "閲覧"),
    EDIT("EDIT", "編集");

    private final String id;
    private final String name;

    @Getter
    private static final List<String> permissions = List.of(
            VIEW.getId(),
            EDIT.getId());

    ClientScreenPermission(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static ClientScreenPermission fromId(String value) {
        for (ClientScreenPermission item : ClientScreenPermission.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "雇用形態権限", value));
    }
}
