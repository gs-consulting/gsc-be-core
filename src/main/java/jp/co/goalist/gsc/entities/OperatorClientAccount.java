package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import jp.co.goalist.gsc.utils.JsonColumnConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorClientAccountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_client_accounts")
@SQLDelete(sql = "UPDATE operator_client_accounts SET is_deleted = true, updated_at = current_timestamp WHERE id = ?")
@SQLRestriction("is_deleted=false")
public class OperatorClientAccount extends BaseEntity {

    /**
     * Reference to Account.id
     */
    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")  // Foreign key column referencing Account table's primary key.
    private Account account;

    @Column
    private String clientName;

    @Column
    private String furiganaName;

    @Column
    private String clientCode;

    @Column
    private String postCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prefecture prefecture;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    @Column
    private String tel;

    @Column
    private String faxCode;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column
    private String employmentType;

    @Column
    private Boolean isInterviewer = false;

    @Column
    private Boolean isIniEducationStaff = false;

    @Column
    private String domainSetting;

    @Column
    private Boolean isDomainEnabled = false;

    @Column
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorClientLocation> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private OperatorClientAccount parent;

    private String oemGroupId;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatorClientAccount> children = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "operator_client_managers",
            joinColumns = @JoinColumn(name = "operator_client_id"),
            inverseJoinColumns = @JoinColumn(name = "operator_client_manager_id"))
    private List<OperatorClientAccount> managers = new ArrayList<>();

    @Column(columnDefinition = "json")
    @Convert(converter = JsonColumnConverter.class)
    private List<String> permissions;

    public void setManagers(List<OperatorClientAccount> managers) {
        if ( this.getManagers() != null ) {
            if ( managers != null ) {
                this.getManagers().clear();
                this.managers = managers;
            }
            else {
                this.managers = new ArrayList<>();
            }
        }
        else {
            if ( managers != null ) {
                this.managers = managers;
            }
        }
    }

    public void setPermissions(List<String> permissions) {
        if ( this.getPermissions() != null ) {
            if ( permissions != null ) {
                this.getPermissions().clear();
                this.permissions = permissions;
            }
            else {
                this.permissions = null;
            }
        }
        else {
            if ( permissions != null ) {
                this.permissions = permissions;
            }
        }
    }

    public void setLocations(List<OperatorClientLocation> locations) {
        if (Objects.isNull(this.locations)) {
            this.locations = new ArrayList<>();
        } else {
            this.locations.clear();
        }

        if (locations != null) {
            this.locations.addAll(locations);
        }
    }
}
