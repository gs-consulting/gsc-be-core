package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Getter
@Setter
@Cacheable
@Cache(region = "operatorTeamCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_teams")
public class OperatorTeam extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String furiganaName;

    @Column
    private String teamCode;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @ManyToMany
    @JoinTable(
            name = "operator_account_teams",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "operator_id"))
    private List<OperatorAccount> staffs = new ArrayList<>();

    public void setStaffs(List<OperatorAccount> staffs) {
        if ( this.getStaffs() != null ) {
            if ( staffs != null ) {
                this.getStaffs().clear();
                this.staffs = staffs;
            }
            else {
                this.staffs = null;
            }
        }
        else {
            if ( staffs != null ) {
                this.staffs = staffs;
            }
        }
    }
}