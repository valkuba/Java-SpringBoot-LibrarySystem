package pa165.project.notificationstatsservice.facade;

import jakarta.validation.Valid;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import java.util.List;
import java.util.Optional;

/* *
 * Interface for Notification Facade layer
 * */

public interface NotificationFacade {
    List<NotificationDto> getAllNotifications();
    Optional<NotificationDto> getById(Long id);
    NotificationDto createNotification(@Valid CreateNotificationDto dto);
    void deleteNotification(Long id);
    NotificationDto createOverdueNotification(String recipientEmail, String bookTitle);
    List<NotificationDto> getAllSortedByDate();
}
