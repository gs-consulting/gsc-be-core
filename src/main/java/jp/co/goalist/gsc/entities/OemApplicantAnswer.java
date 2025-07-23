package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemApplicantAnswerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_applicant_answers")
public class OemApplicantAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String applicantSurveyId;

    @Column
    private Long questionId;

    @Column
    private Long selectedAnswerId;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column
    private LocalDateTime createdAt;
}
