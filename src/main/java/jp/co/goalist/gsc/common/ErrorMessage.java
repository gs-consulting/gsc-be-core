package jp.co.goalist.gsc.common;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NOT_FOUND("GSC_001", "「%s」が見つかりませんでした"),
    INVALID_EMAIL("GSC_002", "「%s」：メールアドレスの形式ではありません"),
    INVALID_DATA("GSC_003", "「%s」が無効です"),
    UNAUTHORIZED("GSC_004", "この操作を行うにはログインが必要です"),
    PERMISSION_DENIED("GSC_005", "この操作を行う権限がありません"),
    INVALID_DELETION("GSC_006", "%s「%s」使用中なので、削除できません"),
    DUPLICATE_DATA("GSC_007", "%s「%s」は既に存在しています"),
    EMAIL_CHANGED("GSC_008", "メールアドレスが変更できません"),
    INVALID_TOKEN("GSC_009", "トーケンが無効です"),
    EXPIRED_TOKEN("GSC_010", "URLの有効期限が切れています"),
    REQUIRED_FIELD("GSC_011", "「%s」は必須です"),
    INVALID_OPERATOR("GSC_012", "この操作は実行できません"),
    MISMATCH_PWD("GSC_013", "確認パスワードが一致してません"),
    BAD_AUTHENTICATION_ERROR("GSC_014", "ユーザー名またはパスワードが間違っています"),
    PROJECT_GROUPING_ERROR("GSC_015", "同じ案件をまとめるため、この「%s」は必須です"),
    CSV_EXPORT_ERROR("GSC_016", "%sのCSVエクスポートは失敗になりました"),
    BULK_IMPORT_PARSE_ERROR("GSC_017", "アップロードされたファイルの読み込みに失敗しました"),
    BULK_IMPORT_FILE_EMPTY_ERROR("GSC_018", "アップロードされたファイルが空です"),
    BULK_IMPORT_HEADER_COUNT_ERROR("GSC_019", "アップロードされたファイルのヘッダー数が異なります"),
    BULK_IMPORT_HEADER_COLUMN_ERROR("GSC_020", "アップロードされたファイルのヘッダーの次の項目が異なります：%s"),
    BULK_IMPORT_EMPTY_REQUIRED_FIELDS_ERROR("GSC_021", "アップロードされたファイルの必須項目に値がありません。[行数：%s, 項目名：%s]"),
    BULK_IMPORT_NOT_MATCH_PATTERN_FIELDS_ERROR("GSC_022", "アップロードされたファイルのフォーマットが不正です。[行数：%s, 項目名：%s, 値：%s]"),
    BULK_IMPORT_INVALID_FIELDS_ERROR("GSC_023", "アップロードされたファイルに不正な値がありました。[行数：%s, 項目名：%s, 値：%s]"),
    BULK_IMPORT_NEGATIVE_NUMBER_ERROR("GSC_024", "0より大きい値を力してください。[行数：%s, 項目名：%s, 値：%s]"),
    BULK_IMPORT_NOT_UNIQUE_FIELDS_ERROR("GSC_025", "アップロードされたファイルにおいて、値に重複があります。[行数：%s, 項目名：%s, 値：%s]"),
    BULK_IMPORT_LENGTHY_FIELDS_ERROR("GSC_026", "アップロードされたファイルには文字数制限を超える値がありました。[行数：%s, 項目名：%s, 文字数制限：%s, 値: %s]"),
    EMAIL_DOMAIN_CHANGED("GSC_027", "メッセージ用メールアドレスが変更できません"),
    MEDIA_REPORT_DATE_RANGE("GSC_028", "6か月以内の日付範囲を選択してください"),
    MAIL_DOMAIN_SETTING_DUPLICATE("GSC_028", "「%s」ドメイン設定は使用されています"),
    POST_CODE_INVALID("GSC_029", "該当する郵便番号が見つかりませんでした\n再度検索を行ってください"),
    BULK_IMPORT_CRAWLED_DATA_ERROR("GSC_030", "クロールデータのため、媒体IDが変更できません。[行数：%s, 項目名：%s, 値: %s]"),
    BULK_IMPORT_PROJECT_MEDIA_ERROR("GSC_031", "媒体IDは案件No.と関係がない。[行数：%s, 項目名：%s, 値: %s]"),
    BULK_IMPORT_PROJECT_REQUIRED_FIELD_ERROR("GSC_032", "案件No.は必須です。[行数：%s, 項目名：%s, 値: %s]");

    private final String statusCode;
    private final String message;

    ErrorMessage(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
