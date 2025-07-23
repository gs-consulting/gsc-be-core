package jp.co.goalist.gsc.enums;

import java.util.Objects;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * Contains all roles in the system
 */
@Getter
public enum Role {
    OPERATOR("OPERATOR", "運営"),
    OEM("OEM", "OEM"),
    CLIENT("CLIENT", "クライアント");

    private final String id;
    private final String name;

    Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role fromId(String id) {
        for (Role role : Role.values()) {
            if (Objects.equals(role.getId(), id)) {
                return role;
            }
        }
        
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "役割", id));
    }
}
