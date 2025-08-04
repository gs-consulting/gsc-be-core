package jp.co.goalist.gsc.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Cache(region = "accountCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@SQLDelete(sql = "UPDATE accounts SET is_deleted = true, updated_at = current_timestamp WHERE id=?")
@SQLRestriction("is_deleted=false")
@Entity(name = "accounts")
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity implements UserDetails {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String email;

    @Column
    private String fullName;

    @Column
    private String password;

    @Column
    private String role;

    @Column
    private String subRole;

    @Column
    private String resetTokenString;

    @Column
    private String forgotTokenString;

    @Column
    private LocalDateTime tokenExpirationDate;

    @Column
    private boolean enabled;

    @Column
    @Builder.Default
    private Boolean isDeleted = false;

    @Column
    @Builder.Default
    private Boolean isNormalLogin = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}