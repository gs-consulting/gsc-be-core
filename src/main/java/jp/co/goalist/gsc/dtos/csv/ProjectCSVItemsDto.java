package jp.co.goalist.gsc.dtos.csv;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
public class ProjectCSVItemsDto {

    private String projectNumber;

    private String projectName;

    private Long statusId;

    private String branchId;

    private String storeId;

    private Date deadline;

    private String workingHours1;

    private String workingHours2;

    private String workingHours3;

    private String workingDays;

    private Long occupationId;

    private String occupation;

    private String salaryType;

    private Integer salaryAmount;

    private String salaryNotes;

    private Long employmentTypeId;

    private Integer recruitingNumber;

    private String genderRestriction;

    private Integer minAge;

    private Integer maxAge;

    private String notHiringCondition;

    private Long workingPeriodId;

    private Date desiredStartDate;

    private String jobDescription;

    private String remarks;

    private Long interviewVenueId;

    private Date employmentPeriodStart;

    private Date employmentPeriodEnd;

    private Boolean isShiftSystem;

    private String experienceStatus;

    private String portraits;

    private String qualifications;

    private String qualificationNotes;

    private Integer prefectureId;

    private Integer cityId;

    private String ward;

    private String workingHoursNotes;

    private String holidays;

    private String benefits;

    private String abAdjustment;

    private String memo;

    private Integer goalApply;

    private Integer goalInterview;

    private Integer goalOffer;

    private Integer goalAgreement;

    // for Order By only
    public String workplace;

    public Timestamp registeredDate;
}
