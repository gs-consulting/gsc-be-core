package jp.co.goalist.gsc.dtos.applicant;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ApplicantWithLatestMessagesDto {

    String getId();

    String getFullName();

    String getGender();

    LocalDate getBirthday();

    String getAddress();

    LocalDateTime getRepliedDate();
}
