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
@Cache(region = "masterStatusCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "master_statuses")
@EqualsAndHashCode
public class MasterStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String statusName;

    @Column(name = "`order`")
    private Integer order;

    @Column
    private String type;

    @Column
    private String oemGroupId;

    /**
     * Parent Id
     * 
     * This field is based on <code>client</code> field. <p>
     * If <code>client</code> has value, parentId will be OemClientAccount.id. <p>
     * If <code>client</code> is null, parentId will be OperatorClientAccount.id.
     */
    @Column
    private String parentId;
}
