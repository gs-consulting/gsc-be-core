package jp.co.goalist.gsc.dtos.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantUnansweredItemDto {

    private String applicantId;

    private String fullName;

    private String gender;

    private Long age;

    private String address;

    private long isDuplicate;

    private long isValid;

    private long isBlacklist;
}
