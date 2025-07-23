package jp.co.goalist.gsc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Getter
@Setter
@Cacheable
@Cache(region = "prefectureCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "prefectures")
public class Prefecture {

    @Id
    private Integer id;

    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "prefecture", fetch = FetchType.LAZY)
    private List<City> cities;
}
