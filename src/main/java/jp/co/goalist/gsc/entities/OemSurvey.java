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
@Cache(region = "oemSurveyCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_surveys")
public class OemSurvey extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String surveyName;

    @ManyToOne(fetch = FetchType.LAZY)
    private OemClientAccount parent;
    
    @Column
    private String oemGroupId;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OemSurveyQuestion> questions;

    @Column
    private Boolean isDeletable = true;
}