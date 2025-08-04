package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorBranchCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_branches")
public class OperatorBranch extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column(nullable = false)
    private String branchName;

    @Column
    private String furiganaName;

    @Column
    private String branchCode;

    @Column
    private String postCode;

    @ManyToOne
    @ToString.Exclude
    private Prefecture prefecture;

    @ManyToOne
    @ToString.Exclude
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
    private String staffPermission;

    @Column
    private String partTimePermission;

    @Column
    private String parentId;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OperatorStore> stores = new ArrayList<>();

    @OneToMany(mappedBy = "operatorBranch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OperatorClientLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @ToString.Exclude
    private List<OperatorProject> projects = new ArrayList<>();
}
