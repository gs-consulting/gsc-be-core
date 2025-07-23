package jp.co.goalist.gsc.common;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constants class contains all the constants used in the application.
 *
 * @author huyphung
 */
public class Constants {
    public static final String PASSWORD_RULE = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    public static final String POSTCODE_RULE = "^\\d{7}$";
    public static final String EMAIL_RULE = "^[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}$";
    public static final String FURIGANA_RULE = "^[ぁ-んァ-ヶー\\s　]*$";
    public static final String PHONE_RULE = "^[0-9+() -]{1,30}$";
    public static final String FAXCODE_RULE = "^[0-9+() -]{1,30}$";
    public static final String TIME_RULE = "^([01]?[0-9]|2[0-3]):([0-5][0-9])〜([01]?[0-9]|2[0-3]):([0-5][0-9])$";
    public static final Integer TOKEN_LENGTH = 45;
    public static final String TOKEN_PATTERN = "{pwd}&{token}&{email}&{type}";
    public static final String ZIPCODE_GET_FIXED_URL = "http://zipcloud.ibsnet.co.jp/api/search?zipcode=${zipcode}";
    public static final String GS_OEM_DEFAULT_ID = "-1";

    public static final String CSV_EMAIL_RULE = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final String CSV_NUMERIC_RULE = "^[0-9]*$";
    public static final String CSV_BINARY_FLAG_RULE = "^[01]$";
    public static final String CSV_TIME_RULE = "^(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$";
    public static final String CSV_DATE_RULE = "^(?:[12][0-9]{3})/(?:0[1-9]|1[0-2])/(?:0[1-9]|1\\d|2\\d|3[01])$";
    public static final String CSV_INTEGER_LIST_RULE = "^(\\d+\\|)*\\d+$";
    public static final String CSV_CHAR_LIST_RULE = "^(\\S+\\|)*\\S+$";

    public static final String SEARCH_NOT_ENTER = "NONE";
    public static final String SQL_WHERE_OPERATOR = " WHERE ";
    public static final String SQL_AND_OPERATOR = " AND ";
    public static final String SQL_OR_OPERATOR = " OR ";
    public static final String SQL_ASC = "asc";
    public static final String SQL_DESC = "desc";
    public static final List<String> SHIFT_SYSTEM = Arrays.asList("1", "0", "NONE");

    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_NO_SS_FORMAT = "yyyy/MM/dd HH:mm";
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final DateTimeFormatter dateTimeNoSSFormatter = DateTimeFormatter.ofPattern(DATE_TIME_NO_SS_FORMAT);
    public static final SimpleDateFormat dateStrFormatter = new SimpleDateFormat("yyyy/MM/dd");

    public static final String WORKING_HOUR_SEPARATOR = "〜";
    public static final String BOM_CHARACTER = "\uFEFF";
    public static final String[] PROJECT_EXPORT_HEADERS_LIST = new String[]{
            "案件No.", "案件名", "ステータス", "勤務地（都道府県）", "勤務地（市区群）", "勤務地（市区群以下）",
            "支店", "拠点・店舗", "納期", "勤務時間1_開始", "勤務時間1_終了", "勤務時間2_開始", "勤務時間2_終了",
            "勤務時間3_開始", "勤務時間3_終了", "勤務曜日", "職種", "給与_区分", "給与_金額", "給与_備考",
            "雇用形態", "募集人数", "性別制限", "年齢(下限)", "年齢(上限)", "採用しない人", "勤務期間",
            "勤務開始希望日", "仕事内容", "備考", "面接会場", "就業期間_開始", "就業期間_終了", "シフト制",
            "経験の有無", "人物像", "資格", "資格補足", "勤務時間補足", "休日", "福利厚生", "AB判定", "メモ",
            "応募数", "面接数", "内定数", "入社数"
    };
    public static final String[] APPLICANT_EXPORT_HEADERS_LIST = new String[]{
            "応募ID", "名前", "フリガナ", "生年月日", "性別", "案件No.", "メールアドレス", "電話番号",
            "郵便番号", "住所（都道府県）", "住所（市区群）", "住所(その他)", "現在の職業",
            "選考ステータス", "資格", "経験", "担当者", "入社日", "メモ", "媒体ID", "クロールのデータであり"
    };
    public static final String[] MEDIA_APPLICANT_EXPORT_HEADERS_LIST = new String[]{
            "媒体応募ID", "媒体ID", "名前", "フリガナ", "生年月日", "性別", "メールアドレス", "電話番号", "郵便番号", "都道府県",
            "市区町村", "住所その他", "現在の職業", "メモ"
    };
    public static final String[] ADVERTISEMENT_EXPORT_HEADERS_LIST = new String[]{
            "広告名", "広告媒体ID", "区分", "掲載開始日", "掲載終了日", "金額", "メモ"
    };

    public static final Map<String, String> SORT_ORDERS = new HashMap<>() {
        {
            put("asc", Constants.SQL_ASC);
            put("desc", Constants.SQL_DESC);
        }
    };

    public static final Map<String, String> APPLICANT_ARRANGED_BY = new HashMap<>() {{
        put("registeredDateTime", "registeredDateTime");
        put("fullName", "fullName");
        put("selectionStatusId", "selectionStatusId");
        put("masterMediaName", "masterMediaName");
        put("picName", "picName");
        put("projectName", "projectName");
        put("latestContactDateTime", "latestContactDateTime");
    }};

    public static final Map<String, String> PROJECT_APPLICANT_ARRANGED_BY = new HashMap<>() {{
        put("registeredDateTime", "createdAt");
        put("fullName", "fullName");
        put("selectionStatusId", "selectionStatus.itemName");
        put("masterMediaName", "media.mediaName");
        put("picName", "pic.account.fullName");
        put("latestContactDateTime", "a.lstContactDateTime");
    }};

    public static final Map<String, String> PROJECT_ARRANGED_BY = new HashMap<>() {{
        put("registeredDate", "registeredDate");
        put("projectNo", "projectNo");
        put("projectName", "projectName");
        put("statusId", "st.order");
        put("occupation", "occupation");
        put("workplace", "workplace");
        put("abAdjustment", "abAdjustment");
    }};

    public static final String MEDIA_REPORT_TOTAL_TITLE = "合計";

    public static final List<String> REPORT_DATA_COLORS = List.of(
            "#2E7D32", "#388E3C", "#4CAF50", "#81C784", "#A5D6A7", "#C8E6C9", "#E8F5E9",
            "#558B2F", "#689F38", "#7CB342", "#9CCC65", "#C5E1A5", "#DCEDC8", "#F1F8E9",
            "#00695C", "#00796B", "#009688", "#4DB6AC", "#80CBC4", "#B2DFDB", "E0F2F1",
            "#00838F", "#0097A7", "#00ACC1", "#00BCD4", "#4DD0E1", "#80DEEA", "#B2EBF2", "#E0F7FA",
            "#0470B1", "#0288D1", "#039BE5", "#29B6F6", "#4FC3F7", "#81D4FA", "#B3E5FC", "#E1F5FE",
            "#1565C0", "#1976D2", "#2196F3", "#64B5F6", "#90CAF9", "#BBDEFB", "#D5ECFD", "#E3F2FD",
            "#18247C", "#303F9F", "#5C6BC0", "#7986CB", "#9FA8DA", "#C5CAE9", "#DBDFF4", "#E8EAF6",
            "#B71C1C", "#D32F2F", "#F44336", "#E57373", "#EF9A9A", "#FFCDD2", "#FFEBEE",
            "#BB350C", "#D84315", "#F4511E", "#FF7043", "#FF8A65", "#FFAB91", "#FFCCBC", "#FBE9E7",
            "#E65100", "#F57C00", "#FF9800", "#FFB74D", "#FFCC80", "#FFE0B2", "#FFEBCE", "#FFF3E0",
            "#FF6F00", "#FF8F00", "#FFB300", "#FFD54F", "#FFE082", "#FFECB3", "#FFF8E1",
            "#F57F17", "#F49314", "#FBC02D", "#FDD835", "#FFEB3B", "#FFF38C", "#FFF9C4", "#FFFDE7",
            "#827717", "#9E9D24", "#AFB42B", "#C0CA33", "#CDDC39", "#E6EE9C", "#F0F4C3", "#F9FBE7"
    );
}
