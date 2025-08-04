package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Getter
@Setter
@Cacheable
@Cache(region = "oemTeamCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_teams")
@EqualsAndHashCode(callSuper = true)
public class OemTeam extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ToString.Exclude
    @ManyToOne
    private OemGroup oemGroup;

    @ToString.Exclude
    @ManyToOne
    private OemAccount oemParent;

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
            name = "oem_account_teams",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "oem_id"))
    private List<OemAccount> staffs = new ArrayList<>();

    public void setStaffs(List<OemAccount> staffs) {
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
