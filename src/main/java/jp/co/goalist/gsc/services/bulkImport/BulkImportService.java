package jp.co.goalist.gsc.services.bulkImport;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.exceptions.BulkImportParseException;
import jp.co.goalist.gsc.exceptions.NotFoundException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.utils.validation.CsvColumn;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static jp.co.goalist.gsc.common.Constants.BOM_CHARACTER;

public abstract class BulkImportService<T> {
	private final Logger logger = LoggerFactory.getLogger(BulkImportService.class);
	protected CSVParser parser;
	protected Class<T> clazz;
	protected String uploadType;
	protected String[] expectedHeaders;
	protected MultipartFile file;

	public BulkImportService(String uploadType) {
		this.uploadType = uploadType;
		this.expectedHeaders = expectedHeaders();
	}

	abstract void preprocess();

	abstract void saveRecordsToDatabase();

	@Transactional
	public void execute(MultipartFile file) {
		this.file = file;
		preprocess();
		fileImportProcess(file);
	}

	private void fileImportProcess(MultipartFile file) {
		try (Reader reader = new InputStreamReader(BOMInputStream.builder()
				.setInputStream(file.getInputStream())
				.setCharset(StandardCharsets.UTF_8)
				.get())) {
			buildParser(reader);
			validateCsvHeader();
			saveRecordsToDatabase();
		}
		catch (IOException e) {
			logger.error(e.getMessage());
			throw new BulkImportParseException(
					ErrorResponse.builder().statusCode(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getStatusCode())
							.message(String.format(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getMessage())).build(),
					e);
		}
		catch (BulkImportInvalidFileContentException | BadValidationException | NotFoundException e) {
			throw e;
		} 
		catch (Exception e) {
			logger.error("Error occurred while parsing the file.", e);
			throw new BulkImportParseException(
					ErrorResponse.builder().statusCode(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getStatusCode())
							.message(String.format(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getMessage())).build(),
					e);
		}
	}

	protected void buildParser(Reader reader) throws IOException {
		this.parser = CSVFormat.DEFAULT.builder().setHeader(expectedHeaders).build().parse(reader);
	}

	protected CSVParser initializeParser(MultipartFile inputFile) throws IOException {
		BOMInputStream bomIn = BOMInputStream.builder()
				.setInputStream(inputFile.getInputStream())
				.setCharset(StandardCharsets.UTF_8)
				.get();
		Reader reader = new InputStreamReader(bomIn);
		return CSVFormat.DEFAULT.builder().setHeader(expectedHeaders).build().parse(reader);
	}

	private void validateCsvHeader() {
		Optional<CSVRecord> first = parser.stream().findFirst();
		if (first.isEmpty()) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_FILE_EMPTY_ERROR.getStatusCode())
					.message(String.format(ErrorMessage.BULK_IMPORT_FILE_EMPTY_ERROR.getMessage())).build());
		}

		CSVRecord actualHeaders = first.get();
		validateHeaderCount(actualHeaders);
		validateHeaderColumn(actualHeaders);
	}

	private void validateHeaderCount(CSVRecord actualHeaders) {
		if (expectedHeaders.length != actualHeaders.size()) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_HEADER_COUNT_ERROR.getStatusCode())
					.message(String.format(ErrorMessage.BULK_IMPORT_HEADER_COUNT_ERROR.getMessage())).build());
		}
	}

	private void validateHeaderColumn(CSVRecord actualHeaders) {
		Map<String, String> invalidHeaderMap = new HashMap<>();
		for (int i = 0; i < expectedHeaders.length; i++) {
			String header = actualHeaders.get(i);
			if(i==0) {
				header = header.replace(BOM_CHARACTER, "");
			}
			if (!expectedHeaders[i].equals(header)) {
				invalidHeaderMap.put(expectedHeaders[i], header);
			}
		}
		if (!invalidHeaderMap.isEmpty()) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_HEADER_COLUMN_ERROR.getStatusCode()).message(String
							.format(ErrorMessage.BULK_IMPORT_HEADER_COLUMN_ERROR.getMessage(), invalidHeaderMap))
					.build());
		}
	}

	T parseCsvRecordIntoDto(CSVRecord record, List<String> headers) {
		T obj;
		try {
			obj = getGenericClazz().getDeclaredConstructor().newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		Field[] fields = getGenericClazz().getDeclaredFields();

		for (Field field : fields) {
			if (!field.isAnnotationPresent(CsvColumn.class)) {
				continue;
			}
			CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
			validateField(record, csvColumn);
			try {
				setField(record, obj, field, headers);
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return obj;
	}

	private void validateField(CSVRecord record, CsvColumn csvColumn) {
		validateRequiredField(record, csvColumn);
		validatePatternField(record, csvColumn);
	}

	private void validateRequiredField(CSVRecord record, CsvColumn csvColumn) {
		String actualValue = record.get(csvColumn.name());
		if (isRequiredAbsent(csvColumn, actualValue)) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_EMPTY_REQUIRED_FIELDS_ERROR.getStatusCode())
					.message(String.format(ErrorMessage.BULK_IMPORT_EMPTY_REQUIRED_FIELDS_ERROR.getMessage(),
							record.getRecordNumber(), csvColumn.name()))
					.build());
		}
	}

	private boolean isRequiredAbsent(CsvColumn csvColumn, String actualValue) {
		return csvColumn.required() && actualValue.isEmpty();
	}

	private void validatePatternField(CSVRecord record, CsvColumn csvColumn) {
		String actualValue = record.get(csvColumn.name());
		System.out.printf("csvColumn: %s, actualValue: %s\n", csvColumn, actualValue);
		if (doesNotMatchPattern(csvColumn, actualValue)) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_NOT_MATCH_PATTERN_FIELDS_ERROR.getStatusCode())
					.message(String.format(ErrorMessage.BULK_IMPORT_NOT_MATCH_PATTERN_FIELDS_ERROR.getMessage(),
							record.getRecordNumber(), csvColumn.name(), actualValue))
					.build());
		}
	}

	private boolean doesNotMatchPattern(CsvColumn csvColumn, String actualValue) {
		if (actualValue.isBlank() || csvColumn.patterns().length == 0) {
			return false;
		}

		for (String pattern : csvColumn.patterns()) {
			Pattern p = Pattern.compile(pattern);
			if (p.matcher(actualValue).matches()) {
				return false; // Value matches one of the patterns
			}
		}
		return true;
	}

	private void setField(CSVRecord record, T obj, Field field, List<String> headers) throws IllegalAccessException {
		CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
		String rawText = record.get(csvColumn.name());
		if (headers.contains(csvColumn.name())) {
			rawText = replaceNewlineCharacter(rawText);
		}
		Object parsedValue = parseText(field, rawText);
		field.setAccessible(true);
		field.set(obj, parsedValue);
	}

	private Object parseText(Field field, String rawText) {
		if (Objects.isNull(rawText) || rawText.isEmpty() || rawText.equalsIgnoreCase("null")) {
			return null;
		}
		return switch (field.getType().getSimpleName()) {
		case "Integer" -> Integer.parseInt(rawText);
		case "Long" -> Long.parseLong(rawText);
		// Listの場合、List<String>として読み込む
		case "List" -> Arrays.stream(rawText.split("\\|")).toList();
		default -> rawText;
		};
	}

	private Class<T> getGenericClazz() {
		if (clazz != null) {
			return this.clazz;
		}
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return clazz;
	}

	private String[] expectedHeaders() {
		List<String> header = new ArrayList<>();
		Field[] fields = getGenericClazz().getDeclaredFields();
		for (Field field : fields) {
			CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
			if (csvColumn != null) {
				header.add(csvColumn.name());
			}
		}

		return header.toArray(new String[0]);
	}

	private String replaceNewlineCharacter(String content) {
		return Objects.nonNull(content) ? content.replace("\r\n", "\n") : null;
	}
}
