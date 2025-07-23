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
@Cache(region = "oemHistoryCallCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "oem_history_calls")
public class OemHistoryCall extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    private OemApplicant applicant;

    /**
     * Reference to OemClientAccount.id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pic_id")
    private OemClientAccount pic;

    @Column
    private LocalDateTime callStartDate;

    @Column
    private LocalDateTime callEndDate;

    @Column(columnDefinition = "text")
    private String memo;
}
