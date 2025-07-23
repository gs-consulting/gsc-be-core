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
@Cache(region = "operatorClientLocationCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_client_locations")
public class OperatorClientLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operator_client_id")
    private OperatorClientAccount account;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private OperatorBranch operatorBranch;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private OperatorStore operatorStore;

}
