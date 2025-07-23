package jp.co.goalist.gsc.exceptions;

import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BadValidationException extends RuntimeException {

    private final ErrorResponse error;

    public BadValidationException(ErrorResponse error) {
        super();
        this.error = error;
    }

}
