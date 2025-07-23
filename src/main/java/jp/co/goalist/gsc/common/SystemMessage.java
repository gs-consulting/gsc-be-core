package jp.co.goalist.gsc.common;

public enum SystemMessage {

    UNKNOWN_ERROR("SYSTEM001", "処理中に不明なエラーが発生しました。"),
    TIME_PARSE_ERROR("SYSTEM002", "「%s」という日付を解析できません。"),
    BUCKET_CREATION_ERROR("SYSTEM003", "バケット「%s」を作成できません。"),
    IMAGE_UPLOAD_ERROR("SYSTEM004", "「%s」：画像アップロードが失敗しました。"),
    MAIL_SENDING_ERROR("SYSTEM005", "メール送信が失敗しました。"),
    CONSTRAINT_VIOLATION_ERROR("SYSTEM006", "「%s」は正しい形式ではありません。"),
    PAGE_LIMIT_ERROR("SYSTEM007", "ページ制限は1以上である必要があります。"),
    PAGE_NUMBER_ERROR("SYSTEM008", "ページ番号は1以上である必要があります。"),
    LIST_SIZE_ERROR("SYSTEM009", "リストのサイズは[%s]でなければなりません"),
    DATA_MISSING_ERROR("SYSTEM010", "データが無効でエラーが発生しました。"),
    NULL_OBJECT_ERROR("SYSTEM011", "「%s」はnullであってはなりません。"),
    DUPLICATE_DATA("SYSTEM012", "同じデータを作成できません。"),
    BATCH_EXPORT_FAIL("SYSTEM013", "エクスポートのバッチが失敗になりました。"),
    UNSUPPORTED_API("SYSTEM014", "エラーが発生しました\n再度時間をおいてからお試しください");

    private final String statusCode;
    private final String message;

    SystemMessage(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return statusCode + ": " + message;
    }
}
