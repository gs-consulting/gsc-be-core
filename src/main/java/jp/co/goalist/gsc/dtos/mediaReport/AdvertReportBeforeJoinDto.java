package jp.co.goalist.gsc.dtos.mediaReport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertReportBeforeJoinDto {

    private String id;

    private String name;

    private Integer amount;

    private Integer statusType;

    private Long personCount;

    private Integer month;

    private String advertisementId;

    private String applicantId;
}
