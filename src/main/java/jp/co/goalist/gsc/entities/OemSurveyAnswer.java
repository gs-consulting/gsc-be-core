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
@Cache(region = "oemSurveyAnswerCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_survey_answers")
public class OemSurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OemSurveyQuestion question;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column
    private Boolean isFixed = false;
}