package jp.co.goalist.gsc.dtos.csv;


import jp.co.goalist.gsc.utils.validation.CsvColumn;

import java.util.List;

import static jp.co.goalist.gsc.common.Constants.*;


public class ApplicantCsvData {

    @CsvColumn(name = "応募ID")
    public String id;

    @CsvColumn(name = "名前", required = true)
    public String fullName;

    @CsvColumn(name = "フリガナ", patterns = FURIGANA_RULE)
    public String furiganaName;

    @CsvColumn(name = "生年月日", patterns = CSV_DATE_RULE)
    public String birthday;

    @CsvColumn(name = "性別")
    public String gender;

    @CsvColumn(name = "案件No.")
    public String projectNo;

    @CsvColumn(name = "メールアドレス", patterns = CSV_EMAIL_RULE)
    public String email;

    @CsvColumn(name = "電話番号", patterns = CSV_NUMERIC_RULE)
    public String tel;

    @CsvColumn(name = "郵便番号", patterns = CSV_NUMERIC_RULE)
    public String postCode;

    @CsvColumn(name = "住所（都道府県）", patterns = CSV_NUMERIC_RULE)
    public String prefecture;

    @CsvColumn(name = "住所（市区群）")
    public String city;

    @CsvColumn(name = "住所(その他)")
    public String homeAddress;

    @CsvColumn(name = "現在の職業")
    public String occupation;

    @CsvColumn(name = "選考ステータス")
    public String selectionStatusId;

    @CsvColumn(name = "資格", patterns = CSV_CHAR_LIST_RULE)
    public List<String> qualificationIds;

    @CsvColumn(name = "経験", patterns = CSV_CHAR_LIST_RULE)
    public List<String> experienceIds;

    @CsvColumn(name = "担当者")
    public String picId;

    @CsvColumn(name = "入社日", patterns = CSV_DATE_RULE)
    public String joinDate;

    @CsvColumn(name = "メモ")
    public String memo;

    @CsvColumn(name = "媒体ID")
    public String mediaId;

    @CsvColumn(name = "クロールのデータであり")
    public String isCrawledData;
}