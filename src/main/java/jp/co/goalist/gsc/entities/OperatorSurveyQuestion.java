package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

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
@Cache(region = "operatorSurveyQuestionCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_survey_questions")
public class OperatorSurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OperatorSurvey survey;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorSurveyAnswer> answers;

    /**
     * QuestionType enum
     */
    @Column
    private String type;

    @Column
    private Boolean isRequired = true;
}
