package jp.co.goalist.gsc.dtos.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantClientAccountCountDto {

    private String branchId;

    private String parentId;

    private long totalApplicants;

    private long totalUnResponseApplicants;
}
