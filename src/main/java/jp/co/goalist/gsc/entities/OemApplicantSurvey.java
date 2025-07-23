package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemApplicantSurveyCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_applicant_surveys")
public class OemApplicantSurvey {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String url;

    @Column
    private String applicantId;

    @Column
    private String surveyId;

    @Column
    private LocalDateTime sentAt;

    @Column
    private LocalDateTime repliedAt;
}
