package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum MessageType {

    EMAIL("EMAIL", "EMAIL"),
    LINE("LINE", "LINE"),
    SMS("SMS", "SMS");

    private final String id;
    private final String name;

    MessageType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static MessageType fromId(String value) {
        for (MessageType item : MessageType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "雇用形態", value));
    }
}