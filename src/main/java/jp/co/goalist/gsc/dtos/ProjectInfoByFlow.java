package jp.co.goalist.gsc.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ProjectInfoByFlow {

    private String id;

    private String projectNo;

    private String projectName;

    private String statusName;

    private String occupation;

    private Integer goalApply;

    private Integer goalInterview;

    private Integer goalOffer;

    private Integer goalAgreement;

    private String qualifications;

    private String workplace;

    private String abAdjustment;

    private String memo;

    private Timestamp registeredDate;
}
