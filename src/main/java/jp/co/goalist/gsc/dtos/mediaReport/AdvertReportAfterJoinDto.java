package jp.co.goalist.gsc.dtos.mediaReport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertReportAfterJoinDto {

    private String id;

    private String name;

    private Integer amount;

    private String advertisementId;

    private String applicantId;

    private Long oneWeekCount;

    private Long oneMonthCount;

    private Long twoMonthCount;

    private Long threeMonthCount;

    private Long sixMonthCount;
}
