package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Cacheable
@Cache(region = "cityCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "cities")
public class City {

    @Id
    private Integer id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "prefecture_id")
    private Prefecture prefecture;
}
