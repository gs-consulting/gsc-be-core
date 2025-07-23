package jp.co.goalist.gsc.services.bulkImport;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.BlacklistInfoDto;
import jp.co.goalist.gsc.dtos.ProjectMediaDto;
import jp.co.goalist.gsc.dtos.csv.ApplicantCsvData;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.Gender;
import jp.co.goalist.gsc.enums.OccupationType;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.exceptions.BulkImportParseException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.*;
import jp.co.goalist.gsc.services.MasterStatusService;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jp.co.goalist.gsc.common.Constants.APPLICANT_EXPORT_HEADERS_LIST;

@Service
public class ApplicantBulkImportService extends BulkImportService<ApplicantCsvData> {
    private static final int BATCH_SIZE = 500;
    private final Logger logger = LoggerFactory.getLogger(ApplicantBulkImportService.class);
    private final MasterStatusService masterStatusService;
    private final PrefectureService prefectureService;
    private final SelectionStatusRepository selectionStatusRepo;
    private final OperatorProjectRepository operatorProjectRepository;
    private final OemProjectRepository oemProjectRepository;
    private final OperatorApplicantRepository operatorApplicantRepository;
    private final OemApplicantRepository oemApplicantRepository;
    private final BlacklistRepository blacklistRepository;
    private final OperatorClientAccountRepository operatorClientAccountRepository;
    private final OemClientAccountRepository oemClientAccountRepository;
    private final MasterMediaRepository masterMediaRepo;
    private final OperatorAdvertisementRepository operatorAdvertisementRepository;
    private final OemAdvertisementRepository oemAdvertisementRepository;
    private String parentId;
    private String oemGroupId;
    private OperatorClientAccount operatorClientAccount;
    private OemClientAccount oemClientAccount;
    private Map<String, OperatorClientAccount> operatorClientAccounts;
    private Map<String, OemClientAccount> oemClientAccounts;
    private Map<String, OperatorApplicant> operatorApplicants;
    private Map<String, OemApplicant> oemApplicants;
    private Map<Pair<String, String>, String> blacklistOfNameAndTels;
    private Map<Pair<String, String>, String> blacklistOfNameAndEmails;
    private Map<String, MasterStatus> masterStatuses;
    private Map<String, SelectionStatus> selectionStatuses;
    private Map<String, OperatorProject> operatorProjects;
    private Map<String, OemProject> oemProjects;
    private Map<String, Prefecture> prefectures;
    private Map<String, MasterMedia> medias;
    private List<String> hasAdvertisementAllProjects;
    private Map<String, List<ProjectMediaDto>> mediasByProject;

    public ApplicantBulkImportService(MasterStatusService masterStatusService,
                                      PrefectureService prefectureService,
                                      SelectionStatusRepository selectionStatusRepo,
                                      OemProjectRepository oemProjectRepository,
                                      OperatorProjectRepository operatorProjectRepository,
                                      OperatorApplicantRepository operatorApplicantRepository,
                                      OemApplicantRepository oemApplicantRepository,
                                      BlacklistRepository blacklistRepository,
                                      OperatorClientAccountRepository operatorClientAccountRepository,
                                      OemClientAccountRepository oemClientAccountRepository,
                                      MasterMediaRepository masterMediaRepo,
                                      OperatorAdvertisementRepository operatorAdvertisementRepository,
                                      OemAdvertisementRepository oemAdvertisementRepository) {
        super("応募者");
        this.masterStatusService = masterStatusService;
        this.prefectureService = prefectureService;
        this.selectionStatusRepo = selectionStatusRepo;
        this.oemProjectRepository = oemProjectRepository;
        this.operatorProjectRepository = operatorProjectRepository;
        this.operatorApplicantRepository = operatorApplicantRepository;
        this.oemApplicantRepository = oemApplicantRepository;
        this.blacklistRepository = blacklistRepository;
        this.operatorClientAccountRepository = operatorClientAccountRepository;
        this.oemClientAccountRepository = oemClientAccountRepository;
        this.masterMediaRepo = masterMediaRepo;
        this.operatorAdvertisementRepository = operatorAdvertisementRepository;
        this.oemAdvertisementRepository = oemAdvertisementRepository;
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
            List<String> applicantIds = getSingleIDByCSVName("応募ID");
            List<String> projectNos = getSingleIDByCSVName("案件No.");
            List<String> prefectures = getSingleIDByCSVName("住所（都道府県）");
            List<String> occupations = getSingleIDByCSVName("現在の職業");
            List<String> selectionStatuses = getSingleIDByCSVName("選考ステータス");
            List<String> qualifications = getIDListByCSVName("資格");
            List<String> experiences = getIDListByCSVName("経験");
            List<String> picIds = getSingleIDByCSVName("担当者");
            List<String> medias = getSingleIDByCSVName("媒体ID");

            setBlacklists();
            setOemAndOperatorObjects(applicantIds, projectNos, picIds);
            setMasterStatuses(occupations, qualifications, experiences);
            setSelectionStatuses(selectionStatuses);
            setPrefectures(prefectures);
            setMedias(medias);

            // Write and Skip the header
            buildParser(reader);
            parser.iterator().next();
            if (Objects.isNull(oemGroupId)) {
                List<OperatorApplicant> applicants = new ArrayList<>();
                handleOperatorRecords(parser, applicants);

                if (!applicants.isEmpty()) {
                    operatorApplicantRepository.saveAll(applicants);
                }

                // Update duplicate relationships
                operatorApplicantRepository.updateMailDuplicate();
                operatorApplicantRepository.updateTelDuplicate();
            } else {
                List<OemApplicant> applicants = new ArrayList<>();
                handleOemRecords(parser, applicants);

                if (!applicants.isEmpty()) {
                    oemApplicantRepository.saveAll(applicants);
                }

                // Update duplicate relationships
                oemApplicantRepository.updateMailDuplicate();
                oemApplicantRepository.updateTelDuplicate();
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
        List<BlacklistInfoDto> nameAndTels = blacklistRepository.findAllNameAndTelBlacklist(this.parentId, this.oemGroupId);
        List<BlacklistInfoDto> nameAndEmails = blacklistRepository.findAllNameAndEmailBlacklist(this.parentId, this.oemGroupId);
        this.blacklistOfNameAndTels = nameAndTels.stream()
                .collect(Collectors.toMap(i -> Pair.of(i.getFullName(), i.getTelOrEmail()), BlacklistInfoDto::getId));
        this.blacklistOfNameAndEmails = nameAndEmails.stream()
                .collect(Collectors.toMap(i -> Pair.of(i.getFullName(), i.getTelOrEmail()), BlacklistInfoDto::getId));
    }

    private void setOemAndOperatorObjects(List<String> applicantIds,
                                          List<String> projectNos,
                                          List<String> picIds) {
        if (Objects.isNull(this.oemGroupId)) {
            List<OperatorApplicant> applicants = operatorApplicantRepository.findAllApplicantsByIds(applicantIds, this.parentId);
            List<OperatorProject> projects = operatorProjectRepository.findAllProjectsByProjectNo(projectNos, this.parentId);
            List<OperatorClientAccount> accounts = operatorClientAccountRepository.findAllByIds(picIds);

            this.operatorApplicants = applicants.stream()
                    .collect(Collectors.toMap(OperatorApplicant::getId, i -> i));
            this.operatorProjects = projects.stream()
                    .collect(Collectors.toMap(OperatorProject::getProjectNumber, i -> i));
            this.operatorClientAccounts = accounts.stream()
                    .collect(Collectors.toMap(OperatorClientAccount::getId, i -> i));
            this.operatorClientAccount = operatorClientAccountRepository.findById(this.parentId).orElse(null);
        } else {
            List<OemApplicant> applicants = oemApplicantRepository.findAllApplicantsByIds(applicantIds, this.parentId, this.oemGroupId);
            List<OemProject> projects = oemProjectRepository.findAllProjectsByProjectNo(projectNos, this.parentId, this.oemGroupId);
            List<OemClientAccount> accounts = oemClientAccountRepository.findAllByIds(picIds);

            this.oemApplicants = applicants.stream()
                    .collect(Collectors.toMap(OemApplicant::getId, i -> i));
            this.oemProjects = projects.stream()
                    .collect(Collectors.toMap(OemProject::getProjectNumber, i -> i));
            this.oemClientAccounts = accounts.stream()
                    .collect(Collectors.toMap(OemClientAccount::getId, i -> i));
            this.oemClientAccount = oemClientAccountRepository.findById(this.parentId).orElse(null);
        }
    }

    private void setMasterStatuses(List<String> occupations,
                                   List<String> qualifications,
                                   List<String> experiences) {
        this.masterStatuses = masterStatusService.setStatusMap(
                null,
                occupations,
                null,
                null,
                null,
                qualifications,
                experiences,
                this.parentId,
                this.oemGroupId
        );
    }

    private void setSelectionStatuses(List<String> selectionStatuses) {
        List<SelectionStatus> statuses = selectionStatusRepo.findAllStatusBy(selectionStatuses, this.parentId, this.oemGroupId);
        this.selectionStatuses = statuses.stream()
                .collect(Collectors.toMap(status -> status.getId().toString(), i -> i));
    }

    private void setMedias(List<String> medias) {
        List<MasterMedia> masterMedias = masterMediaRepo.findAllByIDs(medias, this.parentId, this.oemGroupId);
        this.medias = masterMedias.stream()
                .collect(Collectors.toMap(MasterMedia::getId, i -> i));

        List<String> mediasExcludedLinkingTypeAll = new ArrayList<>(medias);

        if (Objects.isNull(oemGroupId)) {
            this.hasAdvertisementAllProjects = operatorAdvertisementRepository.checkHasAdvertisementWithAllProjects(this.parentId);
            if (!hasAdvertisementAllProjects.isEmpty()) {
                mediasExcludedLinkingTypeAll.removeAll(hasAdvertisementAllProjects);
            }

            if (!mediasExcludedLinkingTypeAll.isEmpty()) {
                List<ProjectMediaDto> masterMediasByAd = masterMediaRepo.findOperatorMasterMediaByAdvertisement(mediasExcludedLinkingTypeAll, this.parentId);
                this.mediasByProject = masterMediasByAd.stream().collect(Collectors.groupingBy(ProjectMediaDto::getProjectNumber));
            }
        } else {
            this.hasAdvertisementAllProjects = oemAdvertisementRepository.checkHasAdvertisementWithAllProjects(this.parentId, this.oemGroupId);
            if (!hasAdvertisementAllProjects.isEmpty()) {
                mediasExcludedLinkingTypeAll.removeAll(hasAdvertisementAllProjects);
            }

            if (!mediasExcludedLinkingTypeAll.isEmpty()) {
                List<ProjectMediaDto> masterMediasByAd = masterMediaRepo.findOemMasterMediaByAdvertisement(mediasExcludedLinkingTypeAll, this.parentId, this.oemGroupId);
                mediasByProject = masterMediasByAd.stream().collect(Collectors.groupingBy(ProjectMediaDto::getProjectNumber));
            }
        }
    }

    private void setPrefectures(List<String> prefectures) {
        Pair<Map<String, Prefecture>, Map<String, City>> prefCities = prefectureService.getPrefecturesAndCitiesForCSV(
                prefectures,
                null
        );
        this.prefectures = prefCities.getLeft();
    }

    private void validateCsvDtoBefore(CSVRecord record, ApplicantCsvData csvDto, boolean isOperator) {
        validateFieldLength(record, csvDto);
        validatePics(record, csvDto, isOperator);
        validateEnum(record, csvDto);
        validateMasterData(record, csvDto);
    }

    private void validateFieldLength(CSVRecord record, ApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.id) && csvDto.id.length() > 36) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "応募ID", csvDto.id, 36);
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
            CSVUtils.throwLengthException(record.getRecordNumber(), "住所（市区群）", csvDto.city, 255);
        }
        if (Objects.nonNull(csvDto.homeAddress) && csvDto.homeAddress.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "住所(その他)", csvDto.homeAddress, 255);
        }
        if (Objects.nonNull(csvDto.memo) && csvDto.memo.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "メモ", csvDto.memo, 500);
        }
    }

    private void validateEnum(CSVRecord record, ApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.gender) && !csvDto.gender.isEmpty()) {
            if (!Gender.checkValidIdExcludeBoth(csvDto.gender)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "性別", csvDto.gender);
            }
        }

        if (Objects.nonNull(csvDto.occupation) && !csvDto.occupation.isEmpty()) {
            if (!OccupationType.checkValidId(csvDto.occupation)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "雇用形態", csvDto.occupation);
            }
        }
    }

    private void validateMasterData(CSVRecord record, ApplicantCsvData csvDto) {
        if (Objects.nonNull(csvDto.prefecture) && !csvDto.prefecture.isEmpty()) {
            int prefectureId = Integer.parseInt(csvDto.prefecture);
            if (prefectureId < 1 || prefectureId > 47) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "住所（都道府県）", csvDto.prefecture);
            }
        }

        if (Objects.nonNull(csvDto.selectionStatusId) && !selectionStatuses.containsKey(csvDto.selectionStatusId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "選考ステータス", csvDto.selectionStatusId);
        }

        if (Objects.nonNull(csvDto.qualificationIds) && !csvDto.qualificationIds.isEmpty()) {
            if (!masterStatuses.keySet().containsAll(csvDto.qualificationIds)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "資格", csvDto.qualificationIds.toString());
            }
        }

        if (Objects.nonNull(csvDto.experienceIds) && !csvDto.experienceIds.isEmpty()) {
            if (!masterStatuses.keySet().containsAll(csvDto.experienceIds)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "経験", csvDto.experienceIds.toString());
            }
        }
    }

    private void validatePics(CSVRecord record, ApplicantCsvData csvDto, boolean isOperator) {
        if (Objects.nonNull(csvDto.picId)) {
            if (isOperator && !operatorClientAccounts.containsKey(csvDto.picId)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "担当者", csvDto.picId);
            } else if (!isOperator && !oemClientAccounts.containsKey(csvDto.picId)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "担当者", csvDto.picId);
            }
        }
    }

    private void validateOemProjects(ApplicantCsvData csvDto, OemApplicant oemApplicant, long rowNumber) {
        if (Objects.isNull(csvDto.projectNo) && Objects.isNull(oemApplicant.getProject()) ||
                (Objects.nonNull(oemApplicant.getProject()) &&
                        Objects.equals(csvDto.projectNo, oemApplicant.getProject().getProjectNumber()))) {
            return;
        }
        if (Objects.nonNull(csvDto.projectNo) && !oemProjects.containsKey(csvDto.projectNo)) {
            CSVUtils.throwInvalidException(rowNumber, "案件No.", csvDto.projectNo);
        }
    }

    private void validateOperatorProjects(ApplicantCsvData csvDto, OperatorApplicant operatorApplicant, long rowNumber) {
        if (Objects.isNull(csvDto.projectNo) && Objects.isNull(operatorApplicant.getProject()) ||
                (Objects.nonNull(operatorApplicant.getProject()) &&
                        Objects.equals(csvDto.projectNo, operatorApplicant.getProject().getProjectNumber()))) {
            return;
        }
        if (Objects.nonNull(csvDto.projectNo) && !operatorProjects.containsKey(csvDto.projectNo)) {
            CSVUtils.throwInvalidException(rowNumber, "案件No.", csvDto.projectNo);
        }
    }

    private void validateOemMediaId(ApplicantCsvData csvDto, OemApplicant oemApplicant, long rowNumber) {
        if (oemApplicant.getIsCrawledData()) {
            if (!Objects.equals(oemApplicant.getMedia().getId(), csvDto.mediaId)) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_CRAWLED_DATA_ERROR);
            }
            return;
        }

        if (Objects.isNull(csvDto.projectNo)) {
            if (Objects.isNull(csvDto.mediaId) ||
                    (Objects.nonNull(oemApplicant.getMedia()) &&
                            Objects.equals(csvDto.mediaId, oemApplicant.getMedia().getId()))) {
                return;
            } else {
                CSVUtils.throwCustomException(rowNumber, "案件No.", csvDto.projectNo,
                        ErrorMessage.BULK_IMPORT_PROJECT_REQUIRED_FIELD_ERROR);
            }
        } else {
            if ((Objects.nonNull(oemApplicant.getProject()) &&
                    Objects.equals(csvDto.projectNo, oemApplicant.getProject().getProjectNumber())) &&
                    (Objects.isNull(csvDto.mediaId) || (Objects.nonNull(oemApplicant.getMedia()) &&
                            Objects.equals(csvDto.mediaId, oemApplicant.getMedia().getId())))) {
                return;
            }
        }

        if (Objects.nonNull(csvDto.mediaId)) {
            if (this.hasAdvertisementAllProjects.contains(csvDto.mediaId)) {
                return;
            }

            List<ProjectMediaDto> mediaByProject = mediasByProject.get(csvDto.projectNo);

            if (Objects.isNull(mediaByProject) || mediaByProject.isEmpty()) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_PROJECT_MEDIA_ERROR);
            } else if (mediaByProject.stream().filter(i -> Objects.equals(i.getMediaId(), csvDto.mediaId)).findAny()
                    .isEmpty()) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_PROJECT_MEDIA_ERROR);
            }
        }
    }

    private void validateOperatorMediaId(ApplicantCsvData csvDto, OperatorApplicant operatorApplicant, long rowNumber) {
        if (operatorApplicant.getIsCrawledData()) {
            if (!Objects.equals(operatorApplicant.getMedia().getId(), csvDto.mediaId)) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_CRAWLED_DATA_ERROR);
            }
            return;
        }

        if (Objects.isNull(csvDto.projectNo)) {
            if (Objects.isNull(csvDto.mediaId) ||
                    (Objects.nonNull(operatorApplicant.getMedia()) &&
                            Objects.equals(csvDto.mediaId, operatorApplicant.getMedia().getId()))) {
                return;
            } else {
                CSVUtils.throwCustomException(rowNumber, "案件No.", csvDto.projectNo,
                        ErrorMessage.BULK_IMPORT_PROJECT_REQUIRED_FIELD_ERROR);
            }
        } else {
            if ((Objects.nonNull(operatorApplicant.getProject()) &&
                    Objects.equals(csvDto.projectNo, operatorApplicant.getProject().getProjectNumber())) &&
                    (Objects.isNull(csvDto.mediaId) || (Objects.nonNull(operatorApplicant.getMedia()) &&
                            Objects.equals(csvDto.mediaId, operatorApplicant.getMedia().getId())))) {
                return;
            }
        }

        if (Objects.nonNull(csvDto.mediaId)) {
            if (this.hasAdvertisementAllProjects.contains(csvDto.mediaId)) {
                return;
            }

            List<ProjectMediaDto> mediaByProject = mediasByProject.get(csvDto.projectNo);

            if (Objects.isNull(mediaByProject) || mediaByProject.isEmpty()) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_PROJECT_MEDIA_ERROR);
            } else if (mediaByProject.stream().filter(i -> Objects.equals(i.getMediaId(), csvDto.mediaId)).findAny()
                    .isEmpty()) {
                CSVUtils.throwCustomException(rowNumber, "媒体ID", csvDto.mediaId,
                        ErrorMessage.BULK_IMPORT_PROJECT_MEDIA_ERROR);
            }
        }
    }

    private OperatorApplicant convertToOperatorEntity(ApplicantCsvData csvDto, long rowNumber) {
        OperatorApplicant existing = operatorApplicants.get(csvDto.id);
        OperatorApplicant applicant;

        // If is new
        if (Objects.isNull(csvDto.id) || Objects.isNull(existing)) {
            applicant = new OperatorApplicant();
            applicant.setParent(operatorClientAccount);

            applicant.setLstStatusChangeDateTime(Objects.nonNull(csvDto.selectionStatusId) ? LocalDateTime.now() : null);
        } else {
            applicant = existing;

            if (Objects.isNull(applicant.getSelectionStatus())) {
                if (Objects.nonNull(csvDto.selectionStatusId)) {
                    applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                }
            } else {
                if (Objects.isNull(csvDto.selectionStatusId)) {
                    applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                } else if (!Objects.equals(csvDto.selectionStatusId, applicant.getSelectionStatus().getId().toString())) {
                        applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                }
            }
        }

        validateOperatorMediaId(csvDto, applicant, rowNumber);
        validateOperatorProjects(csvDto, applicant, rowNumber);
        return updateOperatorApplicantFields(csvDto, applicant);
    }

    private OperatorApplicant updateOperatorApplicantFields(ApplicantCsvData csvDto, OperatorApplicant applicant) {
        applicant.setFullName(csvDto.fullName);
        applicant.setFuriganaName(csvDto.furiganaName);
        applicant.setBirthday(Objects.nonNull(csvDto.birthday) ? CSVUtils.parseTextToDate(csvDto.birthday) : null);
        applicant.setGender(csvDto.gender);
        applicant.setProject(Objects.nonNull(csvDto.projectNo) ? operatorProjects.get(csvDto.projectNo) : null);
        applicant.setEmail(csvDto.email);
        applicant.setTel(csvDto.tel);
        applicant.setPostCode(csvDto.postCode);
        applicant.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        applicant.setCity(csvDto.city);
        applicant.setHomeAddress(csvDto.homeAddress);
        applicant.setOccupation(csvDto.occupation);
        applicant.setSelectionStatus(Objects.nonNull(csvDto.selectionStatusId) ? selectionStatuses.get(csvDto.selectionStatusId) : null);
        applicant.setQualificationIds(CSVUtils.parseTextToIDList(csvDto.qualificationIds));
        applicant.setExperienceIds(CSVUtils.parseTextToIDList(csvDto.experienceIds));
        applicant.setPic(Objects.nonNull(csvDto.picId) ? operatorClientAccounts.get(csvDto.picId) : null);
        applicant.setHiredDate(Objects.nonNull(csvDto.joinDate) ? CSVUtils.parseTextToDate(csvDto.joinDate) : null);
        applicant.setMemo(csvDto.memo);
        applicant.setMedia(Objects.nonNull(csvDto.mediaId) ? medias.get(csvDto.mediaId) : null);
        return applicant;
    }

    private OemApplicant convertToOemEntity(ApplicantCsvData csvDto, long rowNumber) {
        OemApplicant existing = oemApplicants.get(csvDto.id);
        OemApplicant applicant;

        // If is new
        if (Objects.isNull(csvDto.id) || Objects.isNull(existing)) {
            applicant = new OemApplicant();
            applicant.setParent(oemClientAccount);
            applicant.setOemGroupId(oemGroupId);

            applicant.setLstStatusChangeDateTime(Objects.nonNull(csvDto.selectionStatusId) ? LocalDateTime.now() : null);
        } else {
            applicant = existing;

            if (Objects.isNull(applicant.getSelectionStatus())) {
                if (Objects.nonNull(csvDto.selectionStatusId)) {
                    applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                }
            } else {
                if (Objects.isNull(csvDto.selectionStatusId)) {
                    applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                } else if (!Objects.equals(csvDto.selectionStatusId, applicant.getSelectionStatus().getId().toString())) {
                    applicant.setLstStatusChangeDateTime(LocalDateTime.now());
                }
            }
        }

        validateOemMediaId(csvDto, applicant, rowNumber);
        validateOemProjects(csvDto, applicant, rowNumber);
        return updateOemApplicantFields(csvDto, applicant);
    }

    private OemApplicant updateOemApplicantFields(ApplicantCsvData csvDto, OemApplicant applicant) {
        applicant.setFullName(csvDto.fullName);
        applicant.setFuriganaName(csvDto.furiganaName);
        applicant.setBirthday(Objects.nonNull(csvDto.birthday) ? CSVUtils.parseTextToDate(csvDto.birthday) : null);
        applicant.setGender(csvDto.gender);
        applicant.setProject(Objects.nonNull(csvDto.projectNo) ? oemProjects.get(csvDto.projectNo) : null);
        applicant.setEmail(csvDto.email);
        applicant.setTel(csvDto.tel);
        applicant.setPostCode(csvDto.postCode);
        applicant.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        applicant.setCity(csvDto.city);
        applicant.setHomeAddress(csvDto.homeAddress);
        applicant.setOccupation(csvDto.occupation);
        applicant.setSelectionStatus(Objects.nonNull(csvDto.selectionStatusId) ? selectionStatuses.get(csvDto.selectionStatusId) : null);
        applicant.setQualificationIds(CSVUtils.parseTextToIDList(csvDto.qualificationIds));
        applicant.setExperienceIds(CSVUtils.parseTextToIDList(csvDto.experienceIds));
        applicant.setPic(Objects.nonNull(csvDto.picId) ? oemClientAccounts.get(csvDto.picId) : null);
        applicant.setHiredDate(Objects.nonNull(csvDto.joinDate) ? CSVUtils.parseTextToDate(csvDto.joinDate) : null);
        applicant.setMemo(csvDto.memo);
        applicant.setMedia(Objects.nonNull(csvDto.mediaId) ? medias.get(csvDto.mediaId) : null);
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

    private List<String> getIDListByCSVName(String columnName) throws IOException {
        try (CSVParser newParser = initializeParser(file)) {
            newParser.iterator().next(); // Skip the header
            return StreamSupport.stream(newParser.spliterator(), false)
                    .map(record -> record.get(columnName))
                    .filter(v -> !v.isEmpty() && !v.isBlank())
                    .flatMap(v -> Arrays.stream(v.split("\\|")))
                    .distinct()
                    .toList();
        }
    }

    private void handleOperatorRecords(CSVParser parser, List<OperatorApplicant> applicants) {
        for (CSVRecord record : parser) {
            ApplicantCsvData csvDto = parseCsvRecordIntoDto(record, List.of(APPLICANT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto, true);
            OperatorApplicant applicant = convertToOperatorEntity(csvDto, record.getRecordNumber());
            applicants.add(applicant);

            if (applicants.size() == BATCH_SIZE) {
                operatorApplicantRepository.saveAll(applicants);
                applicants.clear();
            }

            // Update blacklist relationships
            String blacklistId1 = blacklistOfNameAndTels.get(Pair.of(applicant.getFullName(), applicant.getTel()));
            String blacklistId2 = blacklistOfNameAndEmails.get(Pair.of(applicant.getFullName(), applicant.getEmail()));
            applicant.setBlacklist1(blacklistId1);
            applicant.setBlacklist2(blacklistId2);
        }
    }

    private void handleOemRecords(CSVParser parser, List<OemApplicant> applicants) {
        for (CSVRecord record : parser) {
            ApplicantCsvData csvDto = parseCsvRecordIntoDto(record, List.of(APPLICANT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto, false);
            OemApplicant applicant = convertToOemEntity(csvDto, record.getRecordNumber());
            applicants.add(applicant);

            if (applicants.size() == BATCH_SIZE) {
                oemApplicantRepository.saveAll(applicants);
                applicants.clear();
            }

            // Update blacklist relationships
            String blacklistId1 = blacklistOfNameAndTels.get(Pair.of(applicant.getFullName(), applicant.getTel()));
            String blacklistId2 = blacklistOfNameAndEmails.get(Pair.of(applicant.getFullName(), applicant.getEmail()));
            applicant.setBlacklist1(blacklistId1);
            applicant.setBlacklist2(blacklistId2);
        }
    }
}
