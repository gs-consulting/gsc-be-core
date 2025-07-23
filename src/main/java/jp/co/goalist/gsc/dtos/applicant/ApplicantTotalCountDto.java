package jp.co.goalist.gsc.dtos.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantTotalCountDto {

    private String countId;

    private long allApply;

    private long allInterview;

    private long allOffer;

    private long allAgreement;
}
