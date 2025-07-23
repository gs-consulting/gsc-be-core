package jp.co.goalist.gsc.exceptions;

import jp.co.goalist.gsc.gen.dtos.ErrorResponse;

public class CSVException extends RuntimeException {

    private final ErrorResponse error;

    public CSVException(ErrorResponse error) {
        super();
        this.error = error;
    }
}
