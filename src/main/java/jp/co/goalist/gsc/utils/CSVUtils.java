package jp.co.goalist.gsc.utils;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jp.co.goalist.gsc.common.Constants.dateFormatter;

public class CSVUtils {

    public static void throwCustomException(long recordNum, String columnName, String value, ErrorMessage errorMessage) {
        throw new BulkImportInvalidFileContentException(
                ErrorResponse.builder()
                        .statusCode(errorMessage.getStatusCode())
                        .message(String.format(
                                errorMessage.getMessage(),
                                recordNum,
                                columnName,
                                Objects.nonNull(value) ? value : ""
                        ))
                        .build());
    }

    public static void throwInvalidException(long recordNum, String columnName, String value) {
        throw new BulkImportInvalidFileContentException(
                ErrorResponse.builder()
                        .statusCode(ErrorMessage.BULK_IMPORT_INVALID_FIELDS_ERROR.getStatusCode())
                        .message(String.format(
                                ErrorMessage.BULK_IMPORT_INVALID_FIELDS_ERROR.getMessage(),
                                recordNum,
                                columnName,
                                Objects.nonNull(value) ? value : ""
                        ))
                        .build());
    }

    public static void throwLengthException(long recordNum, String columnName, String value, Integer limit) {
        throw new BulkImportInvalidFileContentException(
                ErrorResponse.builder()
                        .statusCode(ErrorMessage.BULK_IMPORT_LENGTHY_FIELDS_ERROR.getStatusCode())
                        .message(String.format(
                                ErrorMessage.BULK_IMPORT_LENGTHY_FIELDS_ERROR.getMessage(),
                                recordNum,
                                columnName,
                                limit,
                                value.length()
                        ))
                        .build());
    }

    public static LocalDate parseTextToDate(String date) {
        return LocalDate.parse(date, dateFormatter);
    }

    public static String parseTextToIDList(List<String> csvData) {
        if (Objects.isNull(csvData) || csvData.isEmpty()) {
            return null;
        }
        return csvData.stream()
                .map(Long::valueOf)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

    }

    public static String addQuote(String str) {
        return str == null ? null : "\"" + str + "\"";
    }
}
