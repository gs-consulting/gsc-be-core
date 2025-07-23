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
@Cache(region = "operatorSurveyCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_surveys")
public class OperatorSurvey extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String surveyName;

    @ManyToOne(fetch = FetchType.LAZY)
    private OperatorClientAccount parent;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorSurveyQuestion> questions;

    @Column
    private Boolean isDeletable = true;
}
