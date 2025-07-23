package jp.co.goalist.gsc.services.bulkImport;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.csv.AdvertisementCsvData;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.AdvertisementType;
import jp.co.goalist.gsc.enums.LinkingType;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.exceptions.BulkImportParseException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.MasterMediaRepository;
import jp.co.goalist.gsc.repositories.OemAdvertisementRepository;
import jp.co.goalist.gsc.repositories.OperatorAdvertisementRepository;
import jp.co.goalist.gsc.utils.CSVUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jp.co.goalist.gsc.common.Constants.ADVERTISEMENT_EXPORT_HEADERS_LIST;

@Service
public class AdvertisementBulkImportService extends BulkImportService<AdvertisementCsvData> {
    private final Logger logger = LoggerFactory.getLogger(AdvertisementBulkImportService.class);
    private static final int BATCH_SIZE = 500;
    private final OperatorAdvertisementRepository operatorAdvertisementRepository;
    private final OemAdvertisementRepository oemAdvertisementRepository;
    private final MasterMediaRepository masterMediaRepo;

    private String parentId;
    private String oemGroupId;
    Map<String, OperatorAdvertisement> operatorAdvertisements = new HashMap<>();
    Map<String, OemAdvertisement> oemAdvertisements = new HashMap<>();
    Map<String, MasterMedia> masterMedias = new HashMap<>();

    public AdvertisementBulkImportService(OperatorAdvertisementRepository operatorAdvertisementRepository,
                                          OemAdvertisementRepository oemAdvertisementRepository,
                                          MasterMediaRepository masterMediaRepo) {
        super("広告");
        this.operatorAdvertisementRepository = operatorAdvertisementRepository;
        this.oemAdvertisementRepository = oemAdvertisementRepository;
        this.masterMediaRepo = masterMediaRepo;
    }

    @Override
    void preprocess() {
        // validate permission
    }

    public void setParentInfo(String parentId, String oemGroupId) {
        this.parentId = parentId;
        this.oemGroupId = oemGroupId;
    }

    @Override
    void saveRecordsToDatabase() {
        readCsvRecords();
    }

    protected void readCsvRecords() {
        try (Reader reader = new InputStreamReader(BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .setCharset(StandardCharsets.UTF_8)
                .get())) {
            List<String> advertisementNames = getSingleIDByCSVName("広告名");
            List<String> mediaIds = getSingleIDByCSVName("広告媒体ID");

//            setAdvertisements(advertisementNames);
            setMasterMedias(mediaIds);

            buildParser(reader);
            parser.iterator().next(); // Skip the header

            if (Objects.isNull(oemGroupId)) {
                List<OperatorAdvertisement> advertisements = new ArrayList<>();
                handleOperatorRecords(parser, advertisements);

                if (!advertisements.isEmpty()) {
                    operatorAdvertisementRepository.saveAll(advertisements);
                }
            } else {
                List<OemAdvertisement> advertisements = new ArrayList<>();
                handleOemRecords(parser, advertisements);

                if (!advertisements.isEmpty()) {
                    oemAdvertisementRepository.saveAll(advertisements);
                }
            }

        } catch (BulkImportInvalidFileContentException | AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while parsing the file.", e);
            throw new BulkImportParseException(
                    ErrorResponse.builder().statusCode(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getStatusCode())
                            .message(String.format(ErrorMessage.BULK_IMPORT_PARSE_ERROR.getMessage())).build(),
                    e);
        }
    }

    private void setAdvertisements(List<String> names) {
        if (Objects.isNull(this.oemGroupId)) {
            List<OperatorAdvertisement> advertisements = operatorAdvertisementRepository.findAdvertisementsBy(names, this.parentId);
            this.operatorAdvertisements = advertisements.stream()
                    .collect(Collectors.toMap(OperatorAdvertisement::getName, ad -> ad));

        } else {
            List<OemAdvertisement> advertisements = oemAdvertisementRepository.findAdvertisementsBy(names, this.parentId, this.oemGroupId);
            this.oemAdvertisements = advertisements.stream()
                    .collect(Collectors.toMap(OemAdvertisement::getName, ad -> ad));
        }
    }

    private void setMasterMedias(List<String> mediaIds) {
        List<MasterMedia> masterMedia = masterMediaRepo.findAllByIDs(mediaIds, this.parentId, this.oemGroupId);
        this.masterMedias = masterMedia.stream()
                .collect(Collectors.toMap(MasterMedia::getId, m -> m));
    }

    private void handleOperatorRecords(CSVParser parser, List<OperatorAdvertisement> advertisements) {
        for (CSVRecord record : parser) {
            AdvertisementCsvData csvDto = parseCsvRecordIntoDto(record, List.of(ADVERTISEMENT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto);
            OperatorAdvertisement advertisement = convertToOperatorEntity(csvDto);
            advertisements.add(advertisement);

            if (advertisements.size() == BATCH_SIZE) {
                operatorAdvertisementRepository.saveAll(advertisements);
                advertisements.clear();
            }
        }
    }

    private void handleOemRecords(CSVParser parser, List<OemAdvertisement> advertisements) {
        for (CSVRecord record : parser) {
            AdvertisementCsvData csvDto = parseCsvRecordIntoDto(record, List.of(ADVERTISEMENT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto);
            OemAdvertisement advertisement = convertToOemEntity(csvDto);
            advertisements.add(advertisement);

            if (advertisements.size() == BATCH_SIZE) {
                oemAdvertisementRepository.saveAll(advertisements);
                advertisements.clear();
            }
        }
    }

    // TODO: do we have updates on advertisement
    private OperatorAdvertisement convertToOperatorEntity(AdvertisementCsvData csvDto) {
//        OperatorAdvertisement existingAdvertisement = operatorAdvertisements.get(csvDto.advertisementName);
        OperatorAdvertisement advertisement;
//
//        if (Objects.isNull(existingAdvertisement)) {
            advertisement = new OperatorAdvertisement();
            advertisement.setParentId(this.parentId);
            advertisement.setLinkingType(LinkingType.ALL.getId());
//        } else {
//            advertisement = existingAdvertisement;
//        }

        return updateOperatorAdvertisementFields(csvDto, advertisement);
    }

    private OperatorAdvertisement updateOperatorAdvertisementFields(AdvertisementCsvData csvDto, OperatorAdvertisement advertisement) {
        advertisement.setName(csvDto.advertisementName);
        advertisement.setMasterMedia(masterMedias.get(csvDto.mediaId));
        advertisement.setType(csvDto.category);
        advertisement.setStartDate(Objects.nonNull(csvDto.publishingStartDate) ? CSVUtils.parseTextToDate(csvDto.publishingStartDate) : null);
        advertisement.setEndDate(Objects.nonNull(csvDto.publishingEndDate) ? CSVUtils.parseTextToDate(csvDto.publishingEndDate) : null);
        advertisement.setAmount(csvDto.amount);
        advertisement.setMemo(csvDto.memo);
        return advertisement;
    }

    private OemAdvertisement convertToOemEntity(AdvertisementCsvData csvDto) {
//        OemAdvertisement existingAdvertisement = oemAdvertisements.get(csvDto.advertisementName);
        OemAdvertisement advertisement;

//        if (Objects.isNull(existingAdvertisement)) {
            advertisement = new OemAdvertisement();
            advertisement.setParentId(this.parentId);
            advertisement.setOemGroupId(this.oemGroupId);
            advertisement.setLinkingType(LinkingType.ALL.getId());
//        } else {
//            advertisement = existingAdvertisement;
//        }

        return updateOemAdvertisementFields(csvDto, advertisement);
    }

    private OemAdvertisement updateOemAdvertisementFields(AdvertisementCsvData csvDto, OemAdvertisement advertisement) {
        advertisement.setName(csvDto.advertisementName);
        advertisement.setMasterMedia(masterMedias.get(csvDto.mediaId));
        advertisement.setType(csvDto.category);
        advertisement.setStartDate(Objects.nonNull(csvDto.publishingStartDate) ? CSVUtils.parseTextToDate(csvDto.publishingStartDate) : null);
        advertisement.setEndDate(Objects.nonNull(csvDto.publishingEndDate) ? CSVUtils.parseTextToDate(csvDto.publishingEndDate) : null);
        advertisement.setAmount(csvDto.amount);
        advertisement.setMemo(csvDto.memo);
        return advertisement;
    }

    private void validateCsvDtoBefore(CSVRecord record, AdvertisementCsvData csvDto) {
        validateFieldLength(record, csvDto);
        validateEnum(record, csvDto);
        validateDateRange(record, csvDto);
        validatePositiveNumber(record, csvDto.amount, "金額");
        validateMediaId(record, csvDto);
    }

    private void validateFieldLength(CSVRecord record, AdvertisementCsvData csvDto) {
        if (Objects.nonNull(csvDto.advertisementName) && csvDto.advertisementName.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "広告名", csvDto.advertisementName, 255);
        }

        if (Objects.nonNull(csvDto.memo) && csvDto.memo.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "メモ", csvDto.memo, 500);
        }
    }

    private void validateEnum(CSVRecord record, AdvertisementCsvData csvDto) {
        if (Objects.nonNull(csvDto.category) && !csvDto.category.isEmpty()) {
            if (!AdvertisementType.checkExistingType(csvDto.category)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "区分", csvDto.category);
            }
        }
    }

    private void validateDateRange(CSVRecord record, AdvertisementCsvData csvDto) {
        if (Objects.nonNull(csvDto.publishingStartDate) && Objects.nonNull(csvDto.publishingEndDate)) {
            LocalDate startDate = CSVUtils.parseTextToDate(csvDto.publishingStartDate);
            LocalDate endDate = CSVUtils.parseTextToDate(csvDto.publishingEndDate);
            if (startDate.isAfter(endDate)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "掲載終了日", csvDto.publishingEndDate);
            }
        }
    }

    private void validatePositiveNumber(CSVRecord record, Integer number, String columnName) {
        if (Objects.nonNull(number) && number < 1) {
            throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.BULK_IMPORT_NEGATIVE_NUMBER_ERROR.getMessage())
                    .message(String.format(ErrorMessage.BULK_IMPORT_NEGATIVE_NUMBER_ERROR.getStatusCode(),
                            record.getRecordNumber(), columnName, number))
                    .build());
        }
    }

    private void validateMediaId(CSVRecord record, AdvertisementCsvData csvDto) {
        if (Objects.nonNull(csvDto.mediaId) && Objects.isNull(masterMedias.get(csvDto.mediaId))) {
			throw new BulkImportInvalidFileContentException(ErrorResponse.builder()
					.statusCode(ErrorMessage.BULK_IMPORT_INVALID_FIELDS_ERROR.getStatusCode())
					.message(String.format(ErrorMessage.BULK_IMPORT_INVALID_FIELDS_ERROR.getMessage(),
							record.getRecordNumber(), "広告媒体ID", csvDto.mediaId))
					.build());
        }
    }

    private List<String> getSingleIDByCSVName(String columnName) throws IOException {
        try (CSVParser newParser = initializeParser(file)) {
            newParser.iterator().next(); // Skip the header
            return StreamSupport.stream(newParser.spliterator(), false)
                    .map(record -> record.get(columnName))
                    .filter(v -> !v.isEmpty() && !v.isBlank())
                    .distinct()
                    .toList();
        }
    }
}
