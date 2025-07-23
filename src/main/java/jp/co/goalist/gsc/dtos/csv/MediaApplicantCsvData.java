package jp.co.goalist.gsc.dtos.csv;


import jp.co.goalist.gsc.utils.validation.CsvColumn;

import static jp.co.goalist.gsc.common.Constants.*;


public class MediaApplicantCsvData {

    @CsvColumn(name = "媒体応募ID")
    public String mediaApplicantId;

    @CsvColumn(name = "媒体ID")
    public String mediaId;

    @CsvColumn(name = "名前", required = true)
    public String fullName;

    @CsvColumn(name = "フリガナ", patterns = FURIGANA_RULE)
    public String furiganaName;

    @CsvColumn(name = "生年月日", patterns = CSV_DATE_RULE)
    public String birthday;

    @CsvColumn(name = "性別")
    public String gender;

    @CsvColumn(name = "メールアドレス", patterns = CSV_EMAIL_RULE)
    public String email;

    @CsvColumn(name = "電話番号", patterns = CSV_NUMERIC_RULE)
    public String tel;

    @CsvColumn(name = "郵便番号", patterns = CSV_NUMERIC_RULE)
    public String postCode;

    @CsvColumn(name = "都道府県", patterns = CSV_NUMERIC_RULE)
    public String prefecture;

    @CsvColumn(name = "市区町村")
    public String city;

    @CsvColumn(name = "住所その他")
    public String homeAddress;

    @CsvColumn(name = "現在の職業")
    public String occupation;

    @CsvColumn(name = "メモ")
    public String memo;

}
