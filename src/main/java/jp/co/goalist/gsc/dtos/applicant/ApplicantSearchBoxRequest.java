package jp.co.goalist.gsc.dtos.applicant;

import jp.co.goalist.gsc.enums.ApplicantStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSearchBoxRequest {

    private String applicantId = null;

    private String messageType = null;

    private String name = null;

    private Boolean isNameExactly = false;

    private List<String> genders;

    private Integer maxAge = null;

    private Integer minAge = null;

    private List<String> selectionStatusIds;

    private List<ApplicantStatusType> applicantStatuses;

    private List<String> masterMediaIds;

    private String projectName = null;

    private LocalDate applyStartDate = null;

    private LocalDate applyEndDate = null;

    private List<String> prefectures;

    private String city = null;

    private String address = null;

    private List<String> qualificationIds;

    private List<String> experienceIds;

    private Boolean isStatusNotChanged = false;

    private Boolean isUnread = null;

    private Integer pageNumber = null;

    private Integer pageSize = null;

    private String arrangedBy = null;

    private String blacklistId = null;
}
