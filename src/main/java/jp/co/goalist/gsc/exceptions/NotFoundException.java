package jp.co.goalist.gsc.exceptions;

import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorResponse error;

    public NotFoundException(ErrorResponse error) {
        super();
        this.error = error;
    }

}