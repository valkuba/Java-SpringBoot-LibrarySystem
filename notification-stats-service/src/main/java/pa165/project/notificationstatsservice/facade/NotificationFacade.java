package pa165.project.notificationstatsservice.facade;

import jakarta.validation.Valid;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;

import java.util.List;

public interface NotificationFacade {
    List<NotificationDto> getAllNotifications();
    NotificationDto getById(Long id);
    NotificationDto createNotification(@Valid CreateNotificationDto dto);
    void deleteNotification(Long id);
    NotificationDto createOverdueNotification(String recipientEmail, String bookTitle);
    List<NotificationDto> getAllSortedByDate();
}
