package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "operatorHistoryCallCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "operator_history_calls")
public class OperatorHistoryCall extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    private OperatorApplicant applicant;

    /**
     * Reference to OperatorClientAccount.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pic_id")
    private OperatorClientAccount pic;

    @Column
    private LocalDateTime callStartDate;

    @Column
    private LocalDateTime callEndDate;

    @Column(columnDefinition = "text")
    private String memo;
}
