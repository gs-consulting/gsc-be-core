package jp.co.goalist.gsc.enums;

import java.util.Objects;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * This enum represents the sub roles of clients. <p>
 * 
 * if the role is CLIENT, then check the sub role. Otherwise, sub role must be null. <p>
 * 
 * if sub role is OPERATOR, load data from operator_client_accounts table. <p>
 * else if sub role is OEM, load data from oem_client_accounts table. <p>
 */
@Getter
public enum SubRole {

    OPERATOR("OPERATOR"),
    OEM("OEM");

    private final String id;

    SubRole(String id) {
        this.id = id;
    }

    public static SubRole fromId(String id) {
        for (SubRole role : SubRole.values()) {
            if (Objects.equals(role.getId(), id)) {
                return role;
            }
        }
        
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "クライアントの役割", id));
    }
}
