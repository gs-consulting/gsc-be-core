package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorAdvertisementLinkingCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_advertisement_linkings")
@IdClass(OperatorAdvertisementLinkingId.class)
public class OperatorAdvertisementLinking {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Id
    @Column(name = "advertisement_id")
    private String advertisementId;
}
