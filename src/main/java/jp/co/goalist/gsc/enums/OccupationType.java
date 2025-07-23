package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public enum OccupationType {

    STUDENT("STUDENT", "学生"),
    FREELANCE("FREELANCE", "フリーター"),
    EMPLOYEE("EMPLOYEE", "会社員"),
    FOREIGN_STUDENT("FOREIGN_STUDENT", "留学生"),
    SELF_EMPLOYED("SELF_EMPLOYED", "自営業"),
    HOMEMAKER("HOMEMAKER", "主婦・主夫"),
    OTHER("OTHER", "その他");

    private final String id;
    private final String name;

    OccupationType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static OccupationType fromId(String value) {
        for (OccupationType item : OccupationType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "雇用形態", value));
    }

    public static boolean checkValidId(String item) {
        List<String> ids = Arrays.stream(OccupationType.values()).map(OccupationType::getId).toList();
        return ids.contains(item);
    }
}