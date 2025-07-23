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
@Cache(region = "masterMediaCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "master_medias")
@EqualsAndHashCode(callSuper = true)
public class MasterMedia extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String mediaName;

    @Column
    private String mediaCode;

    @Column
    private Integer amount;

    @Column
    private String hexColor;

    @Column(columnDefinition = "text")
    private String memo;
    
    @Column
    private String siteName;

    @Column
    private String loginId;

    @Column
    private String password;

    @Column
    private String parentId;

    @Column
    private String oemGroupId;
}
