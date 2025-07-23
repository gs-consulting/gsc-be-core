package jp.co.goalist.gsc.repositories;

import jakarta.transaction.Transactional;
import jp.co.goalist.gsc.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = """
            from notifications n
            where :searchInput is null or n.title like :searchInput or n.content like :searchInput
            order by n.postingStartDate desc
            """)
    Page<Notification> getNotificationList(@Param("searchInput") String searchInput, Pageable pageable);

    @Query(value = "from notifications n where n.status <> 'PRIVATE' and n.id in (:ids)")
    List<Notification> checkInUseStatus(List<String> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from notifications n where n.id in (:ids) and n.status <> 'PUBLIC'")
    void delete(List<String> ids);

    @Query(value = """
        from notifications n where n.status = 'PUBLIC' and n.postingStartDate <= current_date
        and n.postingEndDate >= current_date order by n.postingStartDate desc""")
    List<Notification> findAllPublicNotifications();

    @Query(value = "from notifications n where n.id = :id and (:status is null or n.status = :status)")
    Optional<Notification> findByIdAndStatus(String id, String status);
}
