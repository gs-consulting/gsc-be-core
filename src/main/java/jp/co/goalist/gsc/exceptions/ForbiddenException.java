package jp.co.goalist.gsc.exceptions;

import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private final ErrorResponse error;

    public ForbiddenException(ErrorResponse error) {
        super();
        this.error = error;
    }
}
