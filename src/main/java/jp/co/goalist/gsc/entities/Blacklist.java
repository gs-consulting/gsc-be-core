package jp.co.goalist.gsc.entities;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "blacklistCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "blacklists")
public class Blacklist extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column
    private String parentId;

    @Column
    private String oemGroupId;

    @Column
    private String fullName;

    @Column
    private LocalDate birthday;

    @Column
    private String tel;

    @Column
    private String email;

    @Column(columnDefinition = "text")
    private String memo;
}