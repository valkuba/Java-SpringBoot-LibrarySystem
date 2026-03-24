package pa165.project.notificationstatsservice.repository;

import pa165.project.notificationstatsservice.model.Notification;
import java.util.List;
import java.util.Optional;

/* *
 * Interface defining Methods of the Notification Repository
 * */
public interface NotificationRepository {
    List<Notification> findAll();
    Optional<Notification> findById(Long id);
    Notification saveNotification(Notification notification);
    void deleteById(Long id);
    List<Notification> findByRecipientEmail (String email);
}
