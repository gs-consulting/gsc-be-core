package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Cacheable
@Cache(region = "operatorStoreCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_stores")
public class OperatorStore extends BaseEntity  {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String furiganaName;

    @ManyToOne
    private OperatorBranch branch;

    @Column
    private String storeCode;

    @Column
    private String postCode;

    @ManyToOne
    private Prefecture prefecture;

    @ManyToOne
    private City city;

    @Column
    private String tel;

    @Column
    private String faxCode;

    @Column
    private String email;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column
    private String parentId;

    @OneToMany(mappedBy = "operatorStore", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorClientLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<OperatorProject> projects = new ArrayList<>();
}
