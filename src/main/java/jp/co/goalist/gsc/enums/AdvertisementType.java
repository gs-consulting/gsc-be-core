package jp.co.goalist.gsc.enums;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

/**
 * 区分
 */
@Getter
public enum AdvertisementType {

    INTERNET("INTERNET", "ネット媒体"),
    PAPER("PAPER", "紙媒体等"),
    COMPANY("COMPANY", "紹介会社");
    
    AdvertisementType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;

    public static boolean checkExistingType(String value) {
        for (AdvertisementType item : AdvertisementType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return true;
            }
        }

        return false;
    }

    @JsonCreator
    public static AdvertisementType fromId(String value) {
        for (AdvertisementType item : AdvertisementType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "区分", value));
    }
}
