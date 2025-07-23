package jp.co.goalist.gsc.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jp.co.goalist.gsc.utils.JsonColumnConverter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "OemAccountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_accounts")
@EqualsAndHashCode(callSuper = true)
public class OemAccount extends BaseEntity {

    /**
     * Reference to Account.id
     */
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "id")  // Foreign key column referencing Account table's primary key.
    private Account account;

    @ManyToOne
    private OemGroup oemGroup;

    @ManyToOne
    private OemAccount parent;

    @Column
    private String fullName;

    @Column
    private String furiganaName;

    @Column
    private String tel;

    @Column
    private String faxCode;

    @Column(columnDefinition = "text")
    private String memo;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonColumnConverter.class)
    private List<String> permissions;

    @ManyToMany
    @JoinTable(
            name = "oem_account_teams",
            joinColumns = @JoinColumn(name = "oem_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<OemTeam> teams = new ArrayList<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OemAccount> groupedAccounts = new ArrayList<>();

    public void setTeams(List<OemTeam> teams) {
        if ( this.getTeams() != null ) {
            if ( teams != null ) {
                this.getTeams().clear();
                this.teams = teams;
            }
            else {
                this.teams = null;
            }
        }
        else {
            if ( teams != null ) {
                this.teams = teams;
            }
        }
    }
}
