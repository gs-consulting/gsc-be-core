package jp.co.goalist.gsc.services.bulkImport;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.BlacklistInfoDto;
import jp.co.goalist.gsc.dtos.csv.MediaApplicantCsvData;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Gender;
import jp.co.goalist.gsc.enums.OccupationType;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.exceptions.BulkImportParseException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.PrefectureService;
import jp.co.goalist.gsc.utils.CSVUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jp.co.goalist.gsc.common.Constants.MEDIA_APPLICANT_EXPORT_HEADERS_LIST;

@Service
public class MediaApplicantBulkImportService extends BulkImportService<MediaApplicantCsvData> {
    private static final int BATCH_SIZE = 500;
    private final Logger logger = LoggerFactory.getLogger(MediaApplicantBulkImportService.class);
    private final PrefectureService prefectureService;
    private final OperatorApplicantRepository operatorApplicantRepository;
    private final OemApplicantRepository oemApplicantRepository;
    private final BlacklistRepository blacklistRepository;
    private final OperatorClientAccountRepository operatorClientAccountRepository;
    private final OemClientAccountRepository oemClientAccountRepository;
    private final MasterMediaRepository masterMediaRepository;
    private String parentId;
    private String oemGroupId;
    private OperatorClientAccount operatorClientAccount;
    private OemClientAccount oemClientAccount;
    private Map<String, OperatorApplicant> operatorApplicants;
    private Map<String, OemApplicant> oemApplicants;
    private Map<Pair<String, String>, String> blacklistOfNameAndTels;
    private Map<Pair<String, String>, String> blacklistOfNameAndEmails;
    private Map<String, Prefecture> prefectures;

    public MediaApplicantBulkImportService(
            PrefectureService prefectureService,
            OperatorApplicantRepository operatorApplicantRepository,
            OemApplicantRepository oemApplicantRepository,
            BlacklistRepository blacklistRepository,
            OperatorClientAccountRepository operatorClientAccountRepository,
            OemClientAccountRepository oemClientAccountRepository,
            MasterMediaRepository masterMediaRepository) {
        super("媒体応募者");
        this.prefectureService = prefectureService;
        this.operatorApplicantRepository = operatorApplicantRepository;
        this.oemApplicantRepository = oemApplicantRepository;
        this.blacklistRepository = blacklistRepository;
        this.operatorClientAccountRepository = operatorClientAccountRepository;
        this.oemClientAccountRepository = oemClientAccountRepository;
        this.masterMediaRepository = masterMediaRepository;
    }

    @Override
    void preprocess() {
        // validate permission
    }

    public void setParentInfo(String parentId, String oemGroupId) {
        this.parentId = parentId;
        this.oemGroupId = oemGroupId;
    }

    public void setMediaId(String mediaId) {
        MasterMedia masterMedia = masterMediaRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid media ID: " + mediaId));
        this.parentId = masterMedia.getParentId();
        this.oemGroupId = masterMedia.getOemGroupId();
    }

    @Override
    void saveRecordsToDatabase() {
        readCsvRecords();

        // Run after csv progressing is done
        // Update duplicate relationships
        if (Objects.isNull(oemGroupId)) {
            operatorApplicantRepository.updateMailDuplicate();
            operatorApplicantRepository.updateTelDuplicate();
        } else {
            oemApplicantRepository.updateMailDuplicate();
            oemApplicantRepository.updateTelDuplicate();
        }
    }

    protected void readCsvRecords() {
        try (Reader reader = new InputStreamReader(BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .setCharset(StandardCharsets.UTF_8)
                .get())) {
            List<String> mediaApplicantIds = getSingleIDByCSVName("媒体応募ID");
            List<String> prefectures = getSingleIDByCSVName("都道府県");

            setBlacklists();
            setOemAndOperatorObjects(mediaApplicantIds);
            setPrefectures(prefectures);

            // Write and Skip the header
            buildParser(reader);
            parser.iterator().next();
            if (Objects.isNull(oemGroupId)) {
                List<OperatorApplicant> applicants = new ArrayList<>();
                handleOperatorRecords(parser, applicants);
            } else {
                List<OemApplicant> applicants = new ArrayList<>();
                handleOemRecords(parser, applicants);
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

    private void setBlacklists() {
        List<BlacklistInfoDto> nameAndTels = blacklistRepository.findAllNameAndTelBlacklist(this.parentId,
                this.oemGroupId);
        List<BlacklistInfoDto> nameAndEmails = blacklistRepository.findAllNameAndEmailBlacklist(this.parentId,
                this.oemGroupId);
        this.blacklistOfNameAndTels = nameAndTels.stream()
                .collect(Collectors.toMap(i -> Pair.of(i.getFullName(), i.getTelOrEmail()), BlacklistInfoDto::getId));
        this.blacklistOfNameAndEmails = nameAndEmails.stream()
                .collect(Collectors.toMap(i -> Pair.of(i.getFullName(), i.getTelOrEmail()), BlacklistInfoDto::getId));
    }

    private void setOemAndOperatorObjects(List<String> applicantIds) {
        if (Objects.isNull(this.oemGroupId)) {
            List<OperatorApplicant> applicants = operatorApplicantRepository.findAllApplicantsByMediaApplicantIds(applicantIds,
                    this.parentId);
            this.operatorApplicants = applicants.stream()
                    .collect(Collectors.toMap(OperatorApplicant::getMediaApplicantId, i -> i));
            this.operatorClientAccount = operatorClientAccountRepository.findById(this.parentId).orElse(null);
        } else {
            List<OemApplicant> applicants = oemApplicantRepository.findAllApplicantsByMediaApplicantIds(applicantIds, this.parentId,
                    this.oemGroupId);
            this.oemApplicants = applicants.stream()
                    .collect(Collectors.toMap(OemApplicant::getMediaApplicantId, i -> i));
            this.oemClientAccount = oemClientAccountRepository.findById(this.parentId).orElse(null);
        }
    }

    private void setPrefectures(List<String> prefectures) {
        Pair<Map<String, Prefecture>, Map<String, City>> prefCities = prefectureService.getPrefecturesAndCitiesForCSV(
                prefectures,
                null);
        this.prefectures = prefCities.getLeft();
    }

    private void validateCsvDtoBefore(CSVRecord record, MediaApplicantCsvData csvDto, boolean isOperator) {
        validateFieldLength(record, csvDto);
        validateEnum(record, csvDto);
        validateMasterData(record, csvDto);
    }

    private void validateFieldLength(CSVRecord record, MediaApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.mediaApplicantId) && csvDto.mediaApplicantId.length() > 36) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "媒体応募ID", csvDto.mediaApplicantId, 36);
        }
        if (Objects.nonNull(csvDto.fullName) && csvDto.fullName.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "名前", csvDto.fullName, 255);
        }
        if (Objects.nonNull(csvDto.furiganaName) && csvDto.furiganaName.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "フリガナ", csvDto.furiganaName, 255);
        }
        if (Objects.nonNull(csvDto.email) && csvDto.email.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "メールアドレス", csvDto.email, 255);
        }
        if (Objects.nonNull(csvDto.tel) && csvDto.tel.length() > 15) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "電話番号", csvDto.tel, 15);
        }
        if (Objects.nonNull(csvDto.postCode) && csvDto.postCode.length() > 7) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "郵便番号", csvDto.postCode, 7);
        }
        if (Objects.nonNull(csvDto.city) && csvDto.city.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "市区町村", csvDto.city, 255);
        }
        if (Objects.nonNull(csvDto.homeAddress) && csvDto.homeAddress.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "住所その他", csvDto.homeAddress, 255);
        }
    }

    private void validateEnum(CSVRecord record, MediaApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.gender) && !csvDto.gender.isEmpty()) {
            if (!Gender.checkValidId(csvDto.gender)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "性別", csvDto.gender);
            }
        }

        if (Objects.nonNull(csvDto.occupation) && !csvDto.occupation.isEmpty()) {
            if (!OccupationType.checkValidId(csvDto.occupation)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "現在の職業", csvDto.occupation);
            }
        }
    }

    private void validateMasterData(CSVRecord record, MediaApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.prefecture) && !csvDto.prefecture.isEmpty()) {
            int prefectureId = Integer.parseInt(csvDto.prefecture);
            if (prefectureId < 1 || prefectureId > 47) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "都道府県", csvDto.prefecture);
            }
        }
    }

    private OperatorApplicant convertToOperatorEntity(MediaApplicantCsvData csvDto) {
        OperatorApplicant existing = operatorApplicants.get(csvDto.mediaApplicantId);
        OperatorApplicant applicant;

        // If is new
        if (Objects.isNull(csvDto.mediaApplicantId) || Objects.isNull(existing)) {
            applicant = new OperatorApplicant();
            applicant.setParent(operatorClientAccount);
        } else {
            return null; // Skip if applicant already exists, because of duplicate media-applicant
        }

        return updateOperatorApplicantFields(csvDto, applicant);
    }

    private OperatorApplicant updateOperatorApplicantFields(MediaApplicantCsvData csvDto, OperatorApplicant applicant) {
        applicant.setFullName(csvDto.fullName);
        applicant.setFuriganaName(csvDto.furiganaName);
        applicant.setBirthday(Objects.nonNull(csvDto.birthday) ? CSVUtils.parseTextToDate(csvDto.birthday) : null);
        applicant.setGender(csvDto.gender);
        applicant.setEmail(csvDto.email);
        applicant.setTel(csvDto.tel);
        applicant.setPostCode(csvDto.postCode);
        applicant.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        applicant.setCity(csvDto.city);
        applicant.setHomeAddress(csvDto.homeAddress);
        applicant.setOccupation(csvDto.occupation);
        applicant.setIsCrawledData(true);
        applicant.setMediaApplicantId(csvDto.mediaApplicantId);
        applicant.setMemo(csvDto.memo);
        return applicant;
    }

    private OemApplicant convertToOemEntity(MediaApplicantCsvData csvDto) {
        OemApplicant existing = oemApplicants.get(csvDto.mediaApplicantId);
        OemApplicant applicant;

        // If is new
        if (Objects.isNull(csvDto.mediaApplicantId) || Objects.isNull(existing)) {
            applicant = new OemApplicant();
            applicant.setParent(oemClientAccount);
            applicant.setOemGroupId(oemGroupId);
        } else {
            return null; // Skip if applicant already exists, because of duplicate media-applicant
        }

        return updateOemApplicantFields(csvDto, applicant);
    }

    private OemApplicant updateOemApplicantFields(MediaApplicantCsvData csvDto, OemApplicant applicant) {
        applicant.setFullName(csvDto.fullName);
        applicant.setFuriganaName(csvDto.furiganaName);
        applicant.setBirthday(Objects.nonNull(csvDto.birthday) ? CSVUtils.parseTextToDate(csvDto.birthday) : null);
        applicant.setGender(csvDto.gender);
        applicant.setEmail(csvDto.email);
        applicant.setTel(csvDto.tel);
        applicant.setPostCode(csvDto.postCode);
        applicant.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        applicant.setCity(csvDto.city);
        applicant.setHomeAddress(csvDto.homeAddress);
        applicant.setOccupation(csvDto.occupation);
        applicant.setIsCrawledData(true);
        applicant.setMediaApplicantId(csvDto.mediaApplicantId);
        applicant.setMemo(csvDto.memo);
        return applicant;
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

    private void handleOperatorRecords(CSVParser parser, List<OperatorApplicant> applicants) {
        for (CSVRecord record : parser) {
            MediaApplicantCsvData csvDto = parseCsvRecordIntoDto(record, List.of(MEDIA_APPLICANT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto, true);
            OperatorApplicant applicant = convertToOperatorEntity(csvDto);

            if (Objects.isNull(applicant)) {
                continue; // Skip if applicant is null ,because of duplicate media-applicant
            }

            // Update blacklist relationships
            String blacklistId1 = blacklistOfNameAndTels.get(Pair.of(applicant.getFullName(), applicant.getTel()));
            String blacklistId2 = blacklistOfNameAndEmails.get(Pair.of(applicant.getFullName(), applicant.getEmail()));
            applicant.setBlacklist1(blacklistId1);
            applicant.setBlacklist2(blacklistId2);

            applicants.add(applicant);

            if (applicants.size() == BATCH_SIZE) {
                operatorApplicantRepository.saveAll(applicants);
                applicants.clear();
            }
        }
        if (!applicants.isEmpty()) {
            operatorApplicantRepository.saveAll(applicants);
        }
    }

    private void handleOemRecords(CSVParser parser, List<OemApplicant> applicants) {
        for (CSVRecord record : parser) {
            MediaApplicantCsvData csvDto = parseCsvRecordIntoDto(record, List.of(MEDIA_APPLICANT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto, false);
            
            OemApplicant applicant = convertToOemEntity(csvDto);
            if (Objects.isNull(applicant)) {
                continue; // Skip if applicant is null ,because of duplicate media-applicant
            }
            
            // Update blacklist relationships
            String blacklistId1 = blacklistOfNameAndTels.get(Pair.of(applicant.getFullName(), applicant.getTel()));
            String blacklistId2 = blacklistOfNameAndEmails.get(Pair.of(applicant.getFullName(), applicant.getEmail()));
            applicant.setBlacklist1(blacklistId1);
            applicant.setBlacklist2(blacklistId2);
            
            applicants.add(applicant);

            if (applicants.size() == BATCH_SIZE) {
                oemApplicantRepository.saveAll(applicants);
                applicants.clear();
            }           
        }
        if (!applicants.isEmpty()) {
            oemApplicantRepository.saveAll(applicants);
        }
    }
}
