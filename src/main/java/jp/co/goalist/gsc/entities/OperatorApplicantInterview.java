package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorApplicantInterviewCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_applicant_interviews")
public class OperatorApplicantInterview {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    private InterviewCategory category;

    @Column
    private LocalDateTime interviewStartDate;

    @Column
    private LocalDateTime interviewEndDate;

    @Column
    private String picId;

    @Column(columnDefinition = "text")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    private OperatorApplicant applicant;

    private String parentId;
}
