package jp.co.goalist.gsc.entities;

import java.time.LocalDate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.cache.annotation.Cacheable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cache(region = "monthlyCostCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "monthly_costs")
@EqualsAndHashCode
public class MonthlyCost {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private MasterMedia media;

    @Column
    private LocalDate startMonth;

    @Column
    private Integer amount;

    @Column
    private String oemGroupId;

    @Column
    private String parentId;
}
