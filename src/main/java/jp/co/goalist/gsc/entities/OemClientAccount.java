package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import jp.co.goalist.gsc.utils.JsonColumnConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "clientAccountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_client_accounts")
public class OemClientAccount extends BaseEntity {

    /**
     * Reference to Account.id
     */
    @Id
    private String id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")  // Foreign key column referencing Account table's primary key.
    private Account account;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private OemAccount oemAccount;

    @ToString.Exclude
    @ManyToOne
    private OemClientAccount parent;

    @Column
    private String oemGroupId;

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

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OemClientLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OemClientAccount> children = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "oem_client_managers",
            joinColumns = @JoinColumn(name = "oem_client_id"),
            inverseJoinColumns = @JoinColumn(name = "oem_client_manager_id"))
    private List<OemClientAccount> managers = new ArrayList<>();

    @Column(columnDefinition = "json")
    @Convert(converter = JsonColumnConverter.class)
    private List<String> permissions;

    public void setManagers(List<OemClientAccount> managers) {
        if (this.getManagers() != null) {
            if (managers != null) {
                this.getManagers().clear();
                this.managers = managers;
            } else {
                this.managers = null;
            }
        } else {
            if (managers != null) {
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

    public void setLocations(List<OemClientLocation> locations) {
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
