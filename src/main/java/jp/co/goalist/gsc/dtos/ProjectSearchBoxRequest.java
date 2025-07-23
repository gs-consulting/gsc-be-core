package jp.co.goalist.gsc.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearchBoxRequest {

    private String parentId = null;

    private String oemGroupId = null;

    private String employmentType = null;

    private String projectNo = null;

    private String projectName = null;


    private List<String> statusIds;

    private LocalDate registeredStartDate = null;

    private LocalDate registeredEndDate = null;


    private List<String> occupationIds;

    private String isShiftSystem = null;

    private String experience = null;


    private List<String> qualificationIds;

    private String qualificationNotes = null;

    private String prefecture = null;

    private String city = null;

    private String ward = null;

    private String holiday = null;

    private String benefit = null;

    private List<String> branchIds = null;

    private String storeName = null;

    private String workingPeriodId = null;

    private String abAdjustment = null;

    private Integer pageNumber = null;

    private Integer pageSize = null;

    private String arrangedBy = null;

}
