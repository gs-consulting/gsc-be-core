package jp.co.goalist.gsc.services.bulkImport;

import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.dtos.csv.ProjectCsvData;
import jp.co.goalist.gsc.entities.*;
import jp.co.goalist.gsc.enums.AbAdjustment;
import jp.co.goalist.gsc.enums.ExperienceStatus;
import jp.co.goalist.gsc.enums.Gender;
import jp.co.goalist.gsc.enums.SalaryType;
import jp.co.goalist.gsc.enums.WorkingDays;
import jp.co.goalist.gsc.exceptions.BulkImportInvalidFileContentException;
import jp.co.goalist.gsc.exceptions.BulkImportParseException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import jp.co.goalist.gsc.repositories.OemProjectRepository;
import jp.co.goalist.gsc.repositories.OperatorProjectRepository;
import jp.co.goalist.gsc.services.BranchService;
import jp.co.goalist.gsc.services.MasterStatusService;
import jp.co.goalist.gsc.services.PrefectureService;
import jp.co.goalist.gsc.services.StoreService;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jp.co.goalist.gsc.common.Constants.PROJECT_EXPORT_HEADERS_LIST;
import static jp.co.goalist.gsc.common.Constants.WORKING_HOUR_SEPARATOR;

@Service
public class ProjectBulkImportService extends BulkImportService<ProjectCsvData> {
    private static final int BATCH_SIZE = 500;
    private final Logger logger = LoggerFactory.getLogger(ProjectBulkImportService.class);
    private final MasterStatusService masterStatusService;
    private final PrefectureService prefectureService;
    private final BranchService branchService;
    private final StoreService storeService;
    private final OperatorProjectRepository operatorProjectRepository;
    private final OemProjectRepository oemProjectRepository;
    private String parentId;
    private String oemGroupId;
    private String oemParentId;
    private Map<String, MasterStatus> masterStatuses;
    private Map<String, OperatorBranch> operatorBranches;
    private Map<String, OemBranch> oemBranches;
    private Map<String, OperatorStore> operatorStores;
    private Map<String, OemStore> oemStores;
    private Map<String, OperatorProject> operatorProjects;
    private Map<String, OemProject> oemProjects;
    private Map<String, Prefecture> prefectures;
    private Map<String, City> cities;

    public ProjectBulkImportService(MasterStatusService masterStatusService, PrefectureService prefectureService,
                                    BranchService branchService, StoreService storeService,
                                    OemProjectRepository oemProjectRepository,
                                    OperatorProjectRepository operatorProjectRepository1) {
        super("案件");
        this.masterStatusService = masterStatusService;
        this.prefectureService = prefectureService;
        this.branchService = branchService;
        this.storeService = storeService;
        this.oemProjectRepository = oemProjectRepository;
        this.operatorProjectRepository = operatorProjectRepository1;
    }

    protected void readCsvRecords() {
        try (Reader reader = new InputStreamReader(BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .setCharset(StandardCharsets.UTF_8)
                .get())) {
            List<String> statuses = getSingleIDByCSVName("ステータス");
            List<String> occupations = getSingleIDByCSVName("職種");
            List<String> employmentStatues = getSingleIDByCSVName("雇用形態");
            List<String> workingPeriods = getSingleIDByCSVName("勤務期間");
            List<String> interviewVenues = getSingleIDByCSVName("面接会場");
            List<String> qualifications = getIDListByCSVName("資格");


            List<String> prefectures = getIDListByCSVName("勤務地（都道府県）");
            List<String> cities = getIDListByCSVName("勤務地（市区群）");
            List<String> branches = getIDListByCSVName("支店");
            List<String> stores = getIDListByCSVName("拠点・店舗");
            List<String> projectNos = getIDListByCSVName("案件No.");

            setStatusMap(statuses, occupations, employmentStatues, workingPeriods, interviewVenues, qualifications);
            setBranchAndStore(branches, stores);
            setProjects(projectNos);
            setPrefecturesAndCities(prefectures, cities);

            buildParser(reader);
            parser.iterator().next(); // Skip the header

            if (Objects.isNull(oemGroupId)) {
                List<OperatorProject> projects = new ArrayList<>();
                handleOperatorRecords(parser, projects);

                if (!projects.isEmpty()) {
                    operatorProjectRepository.saveAll(projects);
                }
            } else {
                List<OemProject> projects = new ArrayList<>();
                handleOemRecords(parser, projects);

                if (!projects.isEmpty()) {
                    oemProjectRepository.saveAll(projects);
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

    @Override
    void preprocess() {
        // validate permission
    }

    public void setParentInfo(String parentId, String oemParentId, String oemGroupId) {
        this.parentId = parentId;
        this.oemParentId = oemParentId;
        this.oemGroupId = oemGroupId;
    }

    private void setStatusMap(List<String> statuses,
                              List<String> occupations,
                              List<String> employmentStatues,
                              List<String> workingPeriods,
                              List<String> interviewVenues,
                              List<String> qualifications) {
        this.masterStatuses = masterStatusService.setStatusMap(
                statuses,
                occupations,
                employmentStatues,
                workingPeriods,
                interviewVenues,
                qualifications,
                null,
                this.parentId,
                this.oemGroupId
        );
    }

    private void setBranchAndStore(List<String> branchIds, List<String> storeIds) {
        Pair<Map<String, OperatorBranch>, Map<String, OemBranch>> branches = branchService.getBranchesForCSV(
                this.parentId,
                this.oemParentId,
                this.oemGroupId,
                branchIds
        );
        this.operatorBranches = branches.getLeft();
        this.oemBranches = branches.getRight();

        Pair<Map<String, OperatorStore>, Map<String, OemStore>> stores = storeService.getStoresForCSV(
                this.parentId,
                this.oemParentId,
                this.oemGroupId,
                storeIds
        );
        this.operatorStores = stores.getLeft();
        this.oemStores = stores.getRight();
    }

    private void setProjects(List<String> projectNos) {
        if (Objects.isNull(this.oemGroupId)) {
            List<OperatorProject> projects = operatorProjectRepository.findAllProjectsByProjectNo(projectNos, this.parentId);
            this.operatorProjects = projects.stream()
                    .collect(Collectors.toMap(OperatorProject::getProjectNumber, project -> project));

        } else {
            List<OemProject> projects = oemProjectRepository.findAllProjectsByProjectNo(projectNos, this.parentId, this.oemGroupId);
            this.oemProjects = projects.stream()
                    .collect(Collectors.toMap(OemProject::getProjectNumber, project -> project));
        }
    }

    private void setPrefecturesAndCities(List<String> prefectures, List<String> cities) {
        Pair<Map<String, Prefecture>, Map<String, City>> prefCities = prefectureService.getPrefecturesAndCitiesForCSV(
                prefectures,
                cities
        );

        this.prefectures = prefCities.getLeft();
        this.cities = prefCities.getRight();
    }

    @Override
    void saveRecordsToDatabase() {
        readCsvRecords();
    }

    private void validateCsvDtoBefore(CSVRecord record, ProjectCsvData csvDto) {
        validateFieldLength(record, csvDto);
        validateSingleMasterStatus(record, csvDto);
        validateQualifications(record, csvDto);
        validatePrefectureAndCity(record, csvDto);
        validateAge(record, csvDto);
        validateGoalsInfo(record, csvDto);
        validateBranchStore(record, csvDto);
        validateDateRange(record, csvDto);
        validateEnum(record, csvDto);
    }

    private void validateEnum(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.workingDays) && !csvDto.workingDays.isEmpty()) {
            if (!WorkingDays.checkValidIds(csvDto.workingDays)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "勤務曜日", csvDto.workingDays.toString());
            }
        }

        if (Objects.nonNull(csvDto.salaryType) && !csvDto.salaryType.isEmpty()) {
            if (!SalaryType.checkExistingType(csvDto.salaryType)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "給与_区分", csvDto.salaryType);
            }
        }

        if (Objects.nonNull(csvDto.abAdjustment) && !csvDto.abAdjustment.isEmpty()) {
            if (!AbAdjustment.checkExistingTypeExcludeNone(csvDto.abAdjustment)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "AB判定", csvDto.abAdjustment);
            }
        }

        if (Objects.nonNull(csvDto.genderRestriction) && !csvDto.genderRestriction.isEmpty()) {
            if (!Gender.checkValidId(csvDto.genderRestriction)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "性別制限", csvDto.genderRestriction);
            }
        }

        if (Objects.nonNull(csvDto.experienceStatus) && !csvDto.experienceStatus.isEmpty()) {
            if (!ExperienceStatus.checkExistingTypeExcludeNone(csvDto.experienceStatus)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "経験の有無", csvDto.experienceStatus);
            }
        }

    }

    private void validatePrefectureAndCity(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.prefecture) && !csvDto.prefecture.isEmpty()) {
            int prefectureId = Integer.parseInt(csvDto.prefecture);
            if (prefectureId < 1 || prefectureId > 47) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "勤務地（都道府県）", csvDto.prefecture);
            }
        }
    }

    private void validateGoalsInfo(CSVRecord record, ProjectCsvData csvDto) {
        validatePositiveNumber(record, csvDto.goalApply, "応募数");
        validatePositiveNumber(record, csvDto.goalInterview, "面接数");
        validatePositiveNumber(record, csvDto.goalOffer, "内定数");
        validatePositiveNumber(record, csvDto.goalAgreement, "入社数");
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

    private void validateSingleMasterStatus(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.statusId) && !masterStatuses.containsKey(csvDto.statusId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "ステータス", csvDto.statusId);
        }

        if (Objects.nonNull(csvDto.occupationId) && !masterStatuses.containsKey(csvDto.occupationId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "職業", csvDto.occupationId);
        }

        if (Objects.nonNull(csvDto.employmentTypeId) && !masterStatuses.containsKey(csvDto.employmentTypeId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "雇用形態", csvDto.employmentTypeId);
        }

        if (Objects.nonNull(csvDto.workingPeriodId) && !masterStatuses.containsKey(csvDto.workingPeriodId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "勤務期間", csvDto.workingPeriodId);
        }

        if (Objects.nonNull(csvDto.interviewVenueId) && !masterStatuses.containsKey(csvDto.interviewVenueId)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "面接会場", csvDto.interviewVenueId);
        }

    }

    private void validateQualifications(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.qualifications) && !csvDto.qualifications.isEmpty()) {
            if (!masterStatuses.keySet().containsAll(csvDto.qualifications)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "資格", csvDto.qualifications.toString());
            }
        }
    }

    private void validateBranchStore(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.isNull(oemGroupId)) {
            if (Objects.nonNull(csvDto.branchId) && !operatorBranches.containsKey(csvDto.branchId)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "支店", csvDto.branchId);
            }

            if (Objects.nonNull(csvDto.storeId)) {
                if (Objects.isNull(csvDto.branchId)) {
                    CSVUtils.throwInvalidException(record.getRecordNumber(), "支店", null);
                }

                if (!operatorStores.containsKey(csvDto.storeId)) {
                    CSVUtils.throwInvalidException(record.getRecordNumber(), "拠点・店舗", csvDto.storeId);
                }
            }
        } else {
            if (Objects.nonNull(csvDto.branchId) && !oemBranches.containsKey(csvDto.branchId)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "支店", csvDto.branchId);
            }

            if (Objects.nonNull(csvDto.storeId)) {
                if (Objects.isNull(csvDto.branchId)) {
                    CSVUtils.throwInvalidException(record.getRecordNumber(), "支店", null);
                }

                if (!oemStores.containsKey(csvDto.storeId)) {
                    CSVUtils.throwInvalidException(record.getRecordNumber(), "拠点・店舗", csvDto.storeId);
                }
            }
        }
    }

    private void validateDateRange(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.employmentPeriodStart) && Objects.isNull(csvDto.employmentPeriodEnd)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "就業期間_終了", csvDto.branchId);
        }

        if (Objects.isNull(csvDto.employmentPeriodStart) && Objects.nonNull(csvDto.employmentPeriodEnd)) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "就業期間_開始", csvDto.branchId);
        }

        if (Objects.nonNull(csvDto.employmentPeriodStart)) {
            LocalDate startDate = CSVUtils.parseTextToDate(csvDto.employmentPeriodStart);
            LocalDate endDate = CSVUtils.parseTextToDate(csvDto.employmentPeriodEnd);
            if (startDate.isAfter(endDate)) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "就業期間_終了", csvDto.employmentPeriodEnd);
            }
        }
    }

    private Pair<String, String> normalizeTimeFormat(String startTime, String endTime) {
        // h:mm, hh:mm, hh:mm:ss
        String startTimeNormalized = convertToHhMm(startTime);
        String endTimeNormalized = convertToHhMm(endTime);
        return Pair.of(startTimeNormalized, endTimeNormalized);
    }

    private String convertToHhMm(String time) {
        if (Objects.isNull(time)) {
            return null;
        }
        String[] timeArray = time.split(":");

        // for case 6:00:00 -> 6:00
        if (timeArray.length > 2) {
            //get index 0 and 1
            timeArray = Arrays.copyOfRange(timeArray, 0, 2);
        }

        // for case 6:00 -> 06:00
        if (Objects.equals(timeArray[0].length(), 1)) {
            timeArray[0] = "0" + timeArray[0];
        }

        return String.join(":", timeArray);
    }

    private void validateAge(CSVRecord record, ProjectCsvData csvDto) {
        if (csvDto.minAge != null) {
            if (csvDto.minAge < 0) {
                CSVUtils.throwInvalidException(record.getRecordNumber(), "年齢(下限)", csvDto.minAge.toString());
            }
            if (csvDto.maxAge != null) {
                if (csvDto.maxAge < 0 || csvDto.maxAge < csvDto.minAge) {
                    CSVUtils.throwInvalidException(record.getRecordNumber(), "年齢(上限)", csvDto.maxAge.toString());
                }
            }
        } else if (csvDto.maxAge != null && csvDto.maxAge < 0) {
            CSVUtils.throwInvalidException(record.getRecordNumber(), "年齢(上限)", csvDto.maxAge.toString());
        }
    }

    private boolean isNewOperatorProject(ProjectCsvData csvDto, OperatorProject existing) {
        return Objects.isNull(csvDto.projectNumber) || Objects.isNull(existing);
    }

    private boolean isNewOemProject(ProjectCsvData csvDto, OemProject existing) {
        return Objects.isNull(csvDto.projectNumber) || Objects.isNull(existing);
    }

    private OperatorProject convertToOperatorEntity(ProjectCsvData csvDto) {
        OperatorProject existingProject = operatorProjects.get(csvDto.projectNumber);
        OperatorProject project;

        if (isNewOperatorProject(csvDto, existingProject)) {
            project = new OperatorProject();
            project.setProjectNumber(csvDto.projectNumber);
            project.setParentId(this.parentId);
        } else {
            project = existingProject;
        }

        return updateOperatorProjectFields(csvDto, project);
    }

    private OperatorProject updateOperatorProjectFields(ProjectCsvData csvDto, OperatorProject project) {
        project.setProjectNumber(csvDto.projectNumber);
        project.setProjectName(csvDto.projectName);
        project.setStatus(Objects.nonNull(csvDto.statusId) ? masterStatuses.get(csvDto.statusId) : null);
        project.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        project.setCity(Objects.nonNull(csvDto.city) ? cities.get(csvDto.city) : null);
        project.setWard(csvDto.ward);
        project.setBranch(Objects.nonNull(csvDto.branchId) ? operatorBranches.get(csvDto.branchId) : null);
        project.setStore(Objects.nonNull(csvDto.storeId) ? operatorStores.get(csvDto.storeId) : null);
        project.setDeadline(Objects.nonNull(csvDto.deadline) ? CSVUtils.parseTextToDate(csvDto.deadline) : null);
        project.setWorkingDays(Objects.nonNull(csvDto.workingDays) ? csvDto.workingDays : null);
        project.setOccupation(Objects.nonNull(csvDto.occupationId) ? masterStatuses.get(csvDto.occupationId) : null);
        project.setSalaryType(csvDto.salaryType);
        project.setSalaryAmount(csvDto.salaryAmount);
        project.setSalaryNotes(csvDto.salaryNotes);
        project.setEmploymentType(Objects.nonNull(csvDto.employmentTypeId) ? masterStatuses.get(csvDto.employmentTypeId) : null);
        project.setRecruitingNumber(csvDto.recruitingNumber);
        project.setGenderRestriction(csvDto.genderRestriction);
        project.setMinAge(csvDto.minAge);
        project.setMaxAge(csvDto.maxAge);
        project.setNotHiringCondition(csvDto.notHiringCondition);
        project.setWorkingPeriod(Objects.nonNull(csvDto.workingPeriodId) ? masterStatuses.get(csvDto.workingPeriodId) : null);
        project.setWorkingHoursNotes(csvDto.workingHoursNotes);
        project.setDesiredStartDate(Objects.nonNull(csvDto.desiredStartDate) ? CSVUtils.parseTextToDate(csvDto.desiredStartDate) : null);
        project.setJobDescription(csvDto.jobDescription);
        project.setRemarks(csvDto.remarks);
        project.setInterviewVenue(Objects.nonNull(csvDto.interviewVenueId) ? masterStatuses.get(csvDto.interviewVenueId) : null);
        project.setEmploymentPeriodStart(Objects.nonNull(csvDto.employmentPeriodStart) ? CSVUtils.parseTextToDate(csvDto.employmentPeriodStart) : null);
        project.setEmploymentPeriodEnd(Objects.nonNull(csvDto.employmentPeriodEnd) ? CSVUtils.parseTextToDate(csvDto.employmentPeriodEnd) : null);
        project.setIsShiftSystem(processIsShiftSystem(csvDto.isShiftSystem));
        project.setExperienceStatus(csvDto.experienceStatus);
        project.setPortraits(csvDto.portraits);
        project.setQualifications(CSVUtils.parseTextToIDList(csvDto.qualifications));
        project.setQualificationNotes(csvDto.qualificationNotes);
        project.setHolidays(csvDto.holidays);
        project.setBenefits(csvDto.benefits);
        project.setAbAdjustment(csvDto.abAdjustment);
        project.setMemo(csvDto.memo);
        project.setGoalApply(csvDto.goalApply);
        project.setGoalInterview(csvDto.goalInterview);
        project.setGoalOffer(csvDto.goalOffer);
        project.setGoalAgreement(csvDto.goalAgreement);

        handleOperatorWorkingHour(csvDto, project);
        return project;
    }

    private OemProject convertToOemEntity(ProjectCsvData csvDto) {
        OemProject existingProject = oemProjects.get(csvDto.projectNumber);
        OemProject project;

        if (isNewOemProject(csvDto, existingProject)) {
            project = new OemProject();
            project.setProjectNumber(csvDto.projectNumber);
            project.setParentId(this.parentId);
            project.setOemGroupId(this.oemGroupId);
        } else {
            project = existingProject;
        }

        return updateOemProjectFields(csvDto, project);
    }

    private OemProject updateOemProjectFields(ProjectCsvData csvDto, OemProject project) {
        project.setProjectName(csvDto.projectName);
        project.setStatus(Objects.nonNull(csvDto.statusId) ? masterStatuses.get(csvDto.statusId) : null);
        project.setPrefecture(Objects.nonNull(csvDto.prefecture) ? prefectures.get(csvDto.prefecture) : null);
        project.setCity(Objects.nonNull(csvDto.city) ? cities.get(csvDto.city) : null);
        project.setWard(csvDto.ward);
        project.setBranch(Objects.nonNull(csvDto.branchId) ? oemBranches.get(csvDto.branchId) : null);
        project.setStore(Objects.nonNull(csvDto.storeId) ? oemStores.get(csvDto.storeId) : null);
        project.setDeadline(Objects.nonNull(csvDto.deadline) ? CSVUtils.parseTextToDate(csvDto.deadline) : null);
        project.setWorkingDays(Objects.nonNull(csvDto.workingDays) ? csvDto.workingDays : null);
        project.setOccupation(Objects.nonNull(csvDto.occupationId) ? masterStatuses.get(csvDto.occupationId) : null);
        project.setSalaryType(csvDto.salaryType);
        project.setSalaryAmount(csvDto.salaryAmount);
        project.setSalaryNotes(csvDto.salaryNotes);
        project.setEmploymentType(Objects.nonNull(csvDto.employmentTypeId) ? masterStatuses.get(csvDto.employmentTypeId) : null);
        project.setRecruitingNumber(csvDto.recruitingNumber);
        project.setGenderRestriction(csvDto.genderRestriction);
        project.setMinAge(csvDto.minAge);
        project.setMaxAge(csvDto.maxAge);
        project.setNotHiringCondition(csvDto.notHiringCondition);
        project.setWorkingPeriod(Objects.nonNull(csvDto.workingPeriodId) ? masterStatuses.get(csvDto.workingPeriodId) : null);
        project.setWorkingHoursNotes(csvDto.workingHoursNotes);
        project.setDesiredStartDate(Objects.nonNull(csvDto.desiredStartDate) ? CSVUtils.parseTextToDate(csvDto.desiredStartDate) : null);
        project.setJobDescription(csvDto.jobDescription);
        project.setRemarks(csvDto.remarks);
        project.setInterviewVenue(Objects.nonNull(csvDto.interviewVenueId) ? masterStatuses.get(csvDto.interviewVenueId) : null);
        project.setEmploymentPeriodStart(Objects.nonNull(csvDto.employmentPeriodStart) ? CSVUtils.parseTextToDate(csvDto.employmentPeriodStart) : null);
        project.setEmploymentPeriodEnd(Objects.nonNull(csvDto.employmentPeriodEnd) ? CSVUtils.parseTextToDate(csvDto.employmentPeriodEnd) : null);
        project.setIsShiftSystem(processIsShiftSystem(csvDto.isShiftSystem));
        project.setExperienceStatus(csvDto.experienceStatus);
        project.setPortraits(csvDto.portraits);
        project.setQualifications(CSVUtils.parseTextToIDList(csvDto.qualifications));
        project.setQualificationNotes(csvDto.qualificationNotes);
        project.setHolidays(csvDto.holidays);
        project.setBenefits(csvDto.benefits);
        project.setAbAdjustment(csvDto.abAdjustment);
        project.setMemo(csvDto.memo);
        project.setGoalApply(csvDto.goalApply);
        project.setGoalInterview(csvDto.goalInterview);
        project.setGoalOffer(csvDto.goalOffer);
        project.setGoalAgreement(csvDto.goalAgreement);

        handleOemWorkingHour(csvDto, project);
        return project;
    }

    private Boolean processIsShiftSystem(Integer isShiftSystem) {
        return Objects.nonNull(isShiftSystem) ? isShiftSystem == 1 : null;
    }

    private void validateFieldLength(CSVRecord record, ProjectCsvData csvDto) {
        if (Objects.nonNull(csvDto.projectNumber) && csvDto.projectNumber.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "案件No", csvDto.projectNumber, 255);
        }
        if (Objects.nonNull(csvDto.projectName) && csvDto.projectName.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "案件名", csvDto.projectName, 255);
        }
        if (Objects.nonNull(csvDto.salaryNotes) && csvDto.salaryNotes.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "給与_備考", csvDto.salaryNotes, 255);
        }
        if (Objects.nonNull(csvDto.notHiringCondition) && csvDto.notHiringCondition.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "採用しない人", csvDto.notHiringCondition, 255);
        }
        if (Objects.nonNull(csvDto.jobDescription) && csvDto.jobDescription.length() > 1500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "仕事内容", csvDto.jobDescription, 1500);
        }
        if (Objects.nonNull(csvDto.remarks) && csvDto.remarks.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "備考", csvDto.remarks, 500);
        }
        if (Objects.nonNull(csvDto.portraits) && csvDto.portraits.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "人物像", csvDto.portraits, 500);
        }
        if (Objects.nonNull(csvDto.qualificationNotes) && csvDto.qualificationNotes.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "資格補足", csvDto.qualificationNotes, 500);
        }
        if (Objects.nonNull(csvDto.ward) && csvDto.ward.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "勤務地（市区群以下）", csvDto.ward, 255);
        }
        if (Objects.nonNull(csvDto.workingHoursNotes) && csvDto.workingHoursNotes.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "勤務時間補足", csvDto.workingHoursNotes, 500);
        }
        if (Objects.nonNull(csvDto.holidays) && csvDto.holidays.length() > 255) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "休日", csvDto.holidays, 255);
        }
        if (Objects.nonNull(csvDto.benefits) && csvDto.benefits.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "福利厚生", csvDto.benefits, 500);
        }
        if (Objects.nonNull(csvDto.memo) && csvDto.memo.length() > 500) {
            CSVUtils.throwLengthException(record.getRecordNumber(), "メモ", csvDto.memo, 500);
        }
    }

    private void handleOperatorWorkingHour(ProjectCsvData csvDto, OperatorProject project) {
        if (Objects.nonNull(csvDto.startWorkingHours1) && Objects.nonNull(csvDto.endWorkingHours1)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours1, csvDto.endWorkingHours1);
            project.setWorkingHours1(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }

        if (Objects.nonNull(csvDto.startWorkingHours2) && Objects.nonNull(csvDto.endWorkingHours2)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours2, csvDto.endWorkingHours2);
            project.setWorkingHours2(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }

        if (Objects.nonNull(csvDto.startWorkingHours3) && Objects.nonNull(csvDto.endWorkingHours3)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours3, csvDto.endWorkingHours3);
            project.setWorkingHours3(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }
    }

    private void handleOemWorkingHour(ProjectCsvData csvDto, OemProject project) {
        if (Objects.nonNull(csvDto.startWorkingHours1) && Objects.nonNull(csvDto.endWorkingHours1)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours1, csvDto.endWorkingHours1);
            project.setWorkingHours1(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }

        if (Objects.nonNull(csvDto.startWorkingHours2) && Objects.nonNull(csvDto.endWorkingHours2)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours2, csvDto.endWorkingHours2);
            project.setWorkingHours2(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }

        if (Objects.nonNull(csvDto.startWorkingHours3) && Objects.nonNull(csvDto.endWorkingHours3)) {
            Pair<String, String> normalizedTime1 = normalizeTimeFormat(csvDto.startWorkingHours3, csvDto.endWorkingHours3);
            project.setWorkingHours3(normalizedTime1.getLeft().concat(WORKING_HOUR_SEPARATOR).concat(normalizedTime1.getRight()));
        }
    }

    private void handleOperatorRecords(CSVParser parser, List<OperatorProject> projects) {
        for (CSVRecord record : parser) {
            ProjectCsvData csvDto = parseCsvRecordIntoDto(record, List.of(PROJECT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto);
            OperatorProject project = convertToOperatorEntity(csvDto);
            projects.add(project);

            if (projects.size() == BATCH_SIZE) {
                operatorProjectRepository.saveAll(projects);
                projects.clear();
            }
        }
    }

    private void handleOemRecords(CSVParser parser, List<OemProject> projects) {
        for (CSVRecord record : parser) {
            ProjectCsvData csvDto = parseCsvRecordIntoDto(record, List.of(PROJECT_EXPORT_HEADERS_LIST));
            validateCsvDtoBefore(record, csvDto);
            OemProject project = convertToOemEntity(csvDto);
            projects.add(project);

            if (projects.size() == BATCH_SIZE) {
                oemProjectRepository.saveAll(projects);
                projects.clear();
            }
        }
    }
}
