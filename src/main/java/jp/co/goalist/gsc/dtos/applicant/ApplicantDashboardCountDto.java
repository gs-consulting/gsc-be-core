package jp.co.goalist.gsc.dtos.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDashboardCountDto {

    private long todayApply;

    private long todayInterview;

    private long todayOffer;

    private long todayAgreement;

    private long last30DaysApply;

    private long last30DaysInterview;

    private long last30DaysOffer;

    private long last30DaysAgreement;

    private long lastMonthApply;

    private long lastMonthInterview;

    private long lastMonthOffer;

    private long lastMonthAgreement;

    private long twoMonthsApply;

    private long twoMonthsInterview;

    private long twoMonthsOffer;

    private long twoMonthsAgreement;
}
