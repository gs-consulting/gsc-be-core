package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "oemClientLocationCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_client_locations")
public class OemClientLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "oem_client_id")
    private OemClientAccount account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private OemBranch oemBranch;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private OemStore oemStore;
}
