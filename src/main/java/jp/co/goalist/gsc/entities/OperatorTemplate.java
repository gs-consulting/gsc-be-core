package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorTemplateCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_templates")
public class OperatorTemplate extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String parentId;

    @Column
    private String templateName;

    @Column(columnDefinition = "text")
    private String content;
}
