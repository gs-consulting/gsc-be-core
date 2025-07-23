package jp.co.goalist.gsc.exceptions;


import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.Getter;

@Getter
public class BulkImportInvalidFileContentException extends RuntimeException {

	private final ErrorResponse error;

	public BulkImportInvalidFileContentException(ErrorResponse error) {
		super();
		this.error = error;
	}
}