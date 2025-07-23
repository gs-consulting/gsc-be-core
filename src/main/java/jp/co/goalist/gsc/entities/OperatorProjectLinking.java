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
@Cache(region = "operatorProjectLinkingsCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_project_linkings")
@IdClass(OperatorProjectLinkingId.class)
public class OperatorProjectLinking {

    @Id
    @Column(name = "project1_id")
    private String project1Id;

    @Id
    @Column(name = "project2_id")
    private String project2Id;
}
