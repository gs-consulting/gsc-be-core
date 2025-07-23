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
@Cache(region = "oemGroupCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_groups")
@EqualsAndHashCode(callSuper = true)
public class OemGroup extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String oemGroupName;
}
