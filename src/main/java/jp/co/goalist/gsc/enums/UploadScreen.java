package jp.co.goalist.gsc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import jp.co.goalist.gsc.common.ErrorMessage;
import lombok.Getter;

@Getter
public enum UploadScreen {

    PROJECT("PROJECT", "案件"),
    ADVERTISEMENT("ADVERTISEMENT", "広告"),
    APPLICANT("APPLICANT", "応募者"),
    MEDIA_APPLICANT("MEDIA_APPLICANT", "媒体応募者");

    private final String id;
    private final String name;

    UploadScreen(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static UploadScreen fromId(String id) {
        for (UploadScreen screen : UploadScreen.values()) {
            if (screen.getId().equals(id)) {
                return screen;
            }
        }
        throw new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND.getMessage(), "画面", id));
    }
}
