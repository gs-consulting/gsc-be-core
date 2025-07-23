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
@Cache(region = "oemAdvertisementLinkingCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_advertisement_linkings")
@IdClass(OemAdvertisementLinkingId.class)
public class OemAdvertisementLinking {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Id
    @Column(name = "advertisement_id")
    private String advertisementId;
}
