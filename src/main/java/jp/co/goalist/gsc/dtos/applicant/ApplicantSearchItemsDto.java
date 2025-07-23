package jp.co.goalist.gsc.dtos.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSearchItemsDto {

    private String id;

    private Timestamp registeredDateTime;

    private String fullName;

    private String gender;

    private Long age;

    private String address;

    private Long selectionStatusId;

    private String masterMediaName;

    private String picName;

    private String projectName;

    private Timestamp latestContactDateTime;

    private String memo;

    private Boolean isUnread;

    private long hasTel;

    private long hasEmail;

    private long isStatusNotChanged;

    private long isDuplicate;

    private long isDeletable;

    private long isRestricted;

    private long isValid;

    private long isBlacklist;

    private Timestamp interviewDateTime;
}
