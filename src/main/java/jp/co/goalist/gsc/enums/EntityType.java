package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum EntityType {
    NOTIFICATION(1, "お知らせ"),
    CLIENT(2, "クライアント"),
    OEM_ACCOUNT(3, "OEMアカウント"),
    OPERATOR_ACCOUNT(4, "OEMアカウント"),
    CLIENT_ACCOUNT(5, "OEMアカウント"),
    OEM_TEAM(6, "チーム"),
    OPERATOR_TEAM(7, "チーム"),
    OEM_CLIENT_ACCOUNT(8, "OEMのクライアント"),
    OPERATOR_CLIENT_ACCOUNT(9, "運営のクライアント"),
    SELECTION_STATUS(10, "選考ステータス");

    private final long id;
    private final String name;

    EntityType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static EntityType fromId(long value) {
        for (EntityType item : EntityType.values()) {
            if (item.getId() == value) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "type", value));
    }
}
