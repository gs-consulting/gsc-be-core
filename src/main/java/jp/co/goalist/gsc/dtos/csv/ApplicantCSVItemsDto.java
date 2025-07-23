package jp.co.goalist.gsc.dtos.csv;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
public class ApplicantCSVItemsDto {

    public String id;

    public String fullName;

    public String furiganaName;

    public Date birthday;

    public String gender;

    public String projectId;

    public String email;

    public String tel;

    public String postCode;

    public Integer prefecture;

    public String city;

    public String homeAddress;

    public String occupation;

    public Long selectionStatusId;

    public String qualificationIds;

    public String experienceIds;

    public String picId;

    public String memo;

    public String mediaId;

    public Date joinedDate;

    public Boolean isCrawledData;

    // for Order By only
    public String projectName;

    public Timestamp createdAt;

    public Timestamp latestContactDateTime;
}
