package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorSurveyAnswerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_survey_answers")
public class OperatorSurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OperatorSurveyQuestion question;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column
    private Boolean isFixed = false;
}

