package jp.co.goalist.gsc.common;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    CREATED_MSG("CREATE", "Created successfully"),
    GET_MSG("GET", "Get data successfully");

    private final String statusCode;
    private final String message;

    private SuccessMessage(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
