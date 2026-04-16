package pa165.project.notificationstatsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pa165.project.notificationstatsservice.model.Notification;
import java.util.List;
import java.util.Optional;

/* *
 * Interface defining Methods of the Notification Repository
 * */

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.recipientEmail = :recipientEmail")
    List<Notification> findByRecipientEmail(String recipientEmail);

    @Query("SELECT n FROM Notification n ORDER BY n.createdAt DESC")
    List<Notification> findAllByOrderByCreatedAtDesc();
}
