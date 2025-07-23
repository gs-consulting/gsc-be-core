package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum MediaReportChartType {

    TABLE("TABLE", "表"),
    PIE_CHART("PIE_CHART", "円グラフ"),
    PERSON_BAR_CHART("PERSON_BAR_CHART", "応募者のコラムグラフ"),
    COST_BAR_CHART("COST_BAR_CHART", "媒体費のコラムグラフ");

    private final String id;
    private final String name;

    MediaReportChartType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static MediaReportChartType fromId(String value) {
        for (MediaReportChartType item : MediaReportChartType.values()) {
            if (Objects.equals(item.getId(), value)) {
                return item;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "グラフの種類", value));
    }
}
