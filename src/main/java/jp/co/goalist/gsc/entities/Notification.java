package jp.co.goalist.gsc.entities;

import jakarta.persistence.*;
import jp.co.goalist.gsc.enums.PublishingStatus;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(region = "notificationCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate postingStartDate;

    @Column(nullable = false)
    private LocalDate postingEndDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String status;
}
