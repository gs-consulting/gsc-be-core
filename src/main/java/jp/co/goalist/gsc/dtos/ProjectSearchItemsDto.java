package jp.co.goalist.gsc.dtos;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearchItemsDto {

    private String id;

    private String projectNo;

    private String projectName;

    private String statusName;

    private Long statusId;

    private String occupation;

    private Integer totalApplies;

    private Integer totalInterviews;

    private Integer totalOffers;

    private Integer totalAgreements;

    private Integer goalApply;

    private Integer goalInterview;

    private Integer goalOffer;

    private Integer goalAgreement;

    private String qualifications;

    private String workplace;

    private String abAdjustment;

    private String memo;

    private Timestamp registeredDate;

    private Integer isGroupedByOccupation;

    private Integer isGroupedByBranch;

    private Integer hasBranchAuthority;

    private String permission;

    private Integer hasEmploymentTypePermission;
}
