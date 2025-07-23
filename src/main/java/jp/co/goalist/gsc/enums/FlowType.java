package jp.co.goalist.gsc.enums;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum FlowType {

    APPLICATION(1, "応募"),
    INTERVIEW(2, "面接"),
    OFFER(3, "内定"),
    AGREEMENT(4, "入社");

    private final int id;
    private final String value;

    FlowType(int id, String value) {
        this.id = id;
        this.value = value;
    }


    @JsonCreator
    public static FlowType fromId(int id) {
        for (FlowType flowType : FlowType.values()) {
            if (flowType.getId() == id) {
                return flowType;
            }
        }
        
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "選考フロー", id));
    }
    
    /**
     * Get all flow types by order
     * <p>
     * 
     * 応募>面接>内定>入社
     * 
     * @return List of flow types
     */
    public static List<FlowType> getFlowTypes() {
        return List.of(APPLICATION, INTERVIEW, OFFER, AGREEMENT);
    }
}
