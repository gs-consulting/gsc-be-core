package jp.co.goalist.gsc.exceptions;


import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.Getter;

@Getter
public class BulkImportParseException extends RuntimeException {

	private final ErrorResponse error;
	private Throwable originError = null;

	public BulkImportParseException(ErrorResponse error) {
		super();
		this.error = error;
	}

	public BulkImportParseException(ErrorResponse error, Throwable originError) {
		super();
		this.error = error;
		this.originError = originError;
	}

	public ErrorResponse getError() {
		return this.error;
	}

	public Throwable getOriginError() {
		return this.originError;
	}
}
