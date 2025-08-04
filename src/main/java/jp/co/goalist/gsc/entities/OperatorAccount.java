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

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorAccountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_accounts")
@SQLDelete(sql = "UPDATE operator_accounts SET is_deleted = true, updated_at = current_timestamp WHERE id = ?")
@SQLRestriction("is_deleted=false")
@EqualsAndHashCode(callSuper = true)
public class OperatorAccount extends BaseEntity {

    /**
     * Reference to Account.id
     */
    @Id
    private String id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")  // Foreign key column referencing Account table's primary key.
    private Account account;

    @Column
    private String fullName;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonColumnConverter.class)
    private List<String> permissions;

    @Column
    @Builder.Default
    private Boolean isDeleted = false;

    @ManyToMany
    @JoinTable(
            name = "operator_account_teams",
            joinColumns = @JoinColumn(name = "operator_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    @Builder.Default
    private List<OperatorTeam> teams = new ArrayList<>();

    public void setTeams(List<OperatorTeam> teams) {
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
