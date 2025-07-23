package jp.co.goalist.gsc.enums;

import lombok.Getter;

@Getter
public enum TargetName {

    ACCOUNT("アカウント"),
    APPLICANT("応募者"),
    BLACKLIST("選考対象外"),
    NOTIFICATION("お知らせ"),
    STAFF("スタップ`"),
    CLIENT("クライアント"),
    OPERATOR_ACCOUNT("運営アカウント"),
    OEM_ACCOUNT("OEMアカウント"),
    TEAM("チーム"),
    CITY("市区町村"),
    PREFECTURE("都道府県"),
    POST_CODE("郵便番号"),
    BRANCH("支店"),
    STORE("拠点・店舗"),
    INTERVIEW_CATEGORY("面接"),
    MASTER_MEDIA("媒体マスタ登録"),
    PROJECT("案件"),
    ADVERTISEMENT("広告"),
    APPLICANT_MASTER_DATA("応募者のマスタデータ"),
    PROJECT_MASTER_DATA("案件のマスタデータ"),
    GROUP_PROJECT("同一案件"),
    LINKED_PROJECT("関連案件"),
    HISTORY_CALL("通話履歴"),
    TEMPLATE("テンプレート"),
    SURVEY("アンケート");

    private final String targetName;

    TargetName(String targetName) {
        this.targetName = targetName;
    }
}
