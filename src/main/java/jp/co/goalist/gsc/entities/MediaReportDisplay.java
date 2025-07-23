package jp.co.goalist.gsc.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.cache.annotation.Cacheable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "mediaReportDisplayCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "media_report_displays")
@EqualsAndHashCode
public class MediaReportDisplay {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    /*
     * FlowType.id
     */
    @Column
    private Integer flowTypeId;

    @Column
    private Boolean isEnabled;

    /**
     * This field is based on <code>client</code> field and *ClientAccount.getParent(). <p>
     * if clientAccount.parent is null, the parentId will be clientAccount.id. <p>
     * If <code>client</code> has value, parentId will be OemClientAccount.id. <p>
     * If <code>client</code> is null, parentId will be OperatorClientAccount.id.
     */
    @Column
    private String parentId;

    @Column
    private String oemGroupId;
}
