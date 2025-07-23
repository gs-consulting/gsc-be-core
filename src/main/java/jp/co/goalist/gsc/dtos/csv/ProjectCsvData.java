package jp.co.goalist.gsc.dtos.csv;


import jp.co.goalist.gsc.utils.validation.CsvColumn;

import java.util.List;

import static jp.co.goalist.gsc.common.Constants.*;


public class ProjectCsvData {

    @CsvColumn(name = "案件No.", required = true)
    public String projectNumber;

    @CsvColumn(name = "案件名", required = true)
    public String projectName;

    @CsvColumn(name = "ステータス", patterns = CSV_NUMERIC_RULE)
    public String statusId;

    @CsvColumn(name = "勤務地（都道府県）", patterns = CSV_NUMERIC_RULE)
    public String prefecture;

    @CsvColumn(name = "勤務地（市区群）", patterns = CSV_NUMERIC_RULE)
    public String city;

    @CsvColumn(name = "勤務地（市区群以下）")
    public String ward;

    @CsvColumn(name = "支店")
    public String branchId;

    @CsvColumn(name = "拠点・店舗")
    public String storeId;

    @CsvColumn(name = "納期", patterns = CSV_DATE_RULE)
    public String deadline;

    @CsvColumn(name = "勤務時間1_開始", patterns = CSV_TIME_RULE)
    public String startWorkingHours1;

    @CsvColumn(name = "勤務時間1_終了", patterns = CSV_TIME_RULE)
    public String endWorkingHours1;

    @CsvColumn(name = "勤務時間2_開始", patterns = CSV_TIME_RULE)
    public String startWorkingHours2;

    @CsvColumn(name = "勤務時間2_終了", patterns = CSV_TIME_RULE)
    public String endWorkingHours2;

    @CsvColumn(name = "勤務時間3_開始", patterns = CSV_TIME_RULE)
    public String startWorkingHours3;

    @CsvColumn(name = "勤務時間3_終了", patterns = CSV_TIME_RULE)
    public String endWorkingHours3;

    @CsvColumn(name = "勤務曜日", patterns = CSV_CHAR_LIST_RULE)
    public List<String> workingDays;

    @CsvColumn(name = "職種", patterns = CSV_NUMERIC_RULE)
    public String occupationId;

    @CsvColumn(name = "給与_区分")
    public String salaryType;

    @CsvColumn(name = "給与_金額", patterns = CSV_NUMERIC_RULE)
    public Integer salaryAmount;

    @CsvColumn(name = "給与_備考")
    public String salaryNotes;

    @CsvColumn(name = "雇用形態", patterns = CSV_NUMERIC_RULE)
    public String employmentTypeId;

    @CsvColumn(name = "募集人数", patterns = CSV_NUMERIC_RULE)
    public Integer recruitingNumber;

    @CsvColumn(name = "性別制限")
    public String genderRestriction;

    @CsvColumn(name = "年齢(下限)", patterns = CSV_NUMERIC_RULE)
    public Integer minAge;

    @CsvColumn(name = "年齢(上限)", patterns = CSV_NUMERIC_RULE)
    public Integer maxAge;

    @CsvColumn(name = "採用しない人")
    public String notHiringCondition;

    @CsvColumn(name = "勤務期間", patterns = CSV_NUMERIC_RULE)
    public String workingPeriodId;

    @CsvColumn(name = "勤務開始希望日", patterns = CSV_DATE_RULE)
    public String desiredStartDate;

    @CsvColumn(name = "仕事内容")
    public String jobDescription;

    @CsvColumn(name = "備考")
    public String remarks;

    @CsvColumn(name = "面接会場", patterns = CSV_NUMERIC_RULE)
    public String interviewVenueId;

    @CsvColumn(name = "就業期間_開始", patterns = CSV_DATE_RULE)
    public String employmentPeriodStart;

    @CsvColumn(name = "就業期間_終了", patterns = CSV_DATE_RULE)
    public String employmentPeriodEnd;

    @CsvColumn(name = "シフト制", patterns = CSV_BINARY_FLAG_RULE)
    public Integer isShiftSystem;

    @CsvColumn(name = "経験の有無")
    public String experienceStatus;

    @CsvColumn(name = "人物像")
    public String portraits;

    @CsvColumn(name = "資格", patterns = CSV_INTEGER_LIST_RULE)
    public List<String> qualifications;

    @CsvColumn(name = "資格補足")
    public String qualificationNotes;

    @CsvColumn(name = "勤務時間補足")
    public String workingHoursNotes;

    @CsvColumn(name = "休日")
    public String holidays;

    @CsvColumn(name = "福利厚生")
    public String benefits;

    @CsvColumn(name = "AB判定")
    public String abAdjustment;

    @CsvColumn(name = "メモ")
    public String memo;

    @CsvColumn(name = "応募数", patterns = CSV_NUMERIC_RULE)
    public Integer goalApply;

    @CsvColumn(name = "面接数", patterns = CSV_NUMERIC_RULE)
    public Integer goalInterview;

    @CsvColumn(name = "内定数", patterns = CSV_NUMERIC_RULE)
    public Integer goalOffer;

    @CsvColumn(name = "入社数", patterns = CSV_NUMERIC_RULE)
    public Integer goalAgreement;
}