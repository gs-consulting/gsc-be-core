package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemAdvertisementsCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_advertisements")
public class OemAdvertisement extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String oemGroupId;

    @Column
    private String parentId;

    @Column
    private String name;

    @ToString.Exclude
    @ManyToOne
    private MasterMedia masterMedia;

    @Column
    private String type;

    @Column
    private Integer amount;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(columnDefinition = "text")
    private String memo;

    @Column
    private String linkingType;
}
