package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemSurveyQuestionCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_survey_questions")
public class OemSurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String questionText;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OemSurvey survey;

    @ToString.Exclude
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OemSurveyAnswer> answers;

    /**
     * QuestionType enum
     */
    @Column
    private String type;

    @Column
    private Boolean isRequired = true;
}