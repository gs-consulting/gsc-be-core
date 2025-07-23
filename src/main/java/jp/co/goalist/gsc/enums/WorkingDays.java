package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum WorkingDays {

    MON("MON", "月"),
    TUE("TUE", "火"),
    WED("WED", "水"),
    THU("THU", "木"),
    FRI("FRI", "金"),
    SAT("SAT", "土"),
    SUN("SUN", "日"),
    HOLIDAY("HOLIDAY", "祝"),
    SHIFT("SHIFT", "シフト制");

    private final String id;
    private final String name;

    WorkingDays(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static WorkingDays fromId(String id) {
        for (WorkingDays item : WorkingDays.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "勤務曜日", id));
    }

    public static boolean checkValidIds(List<String> items) {
        List<String> ids = Arrays.stream(WorkingDays.values()).map(WorkingDays::getId).toList();
        List<String> result = new ArrayList<>(items);
        result.removeAll(ids);
        return result.isEmpty();
    }
}
