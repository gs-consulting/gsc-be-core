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
@Cache(region = "operatorApplicantAnswerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_applicant_answers")
public class OperatorApplicantAnswer {

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
