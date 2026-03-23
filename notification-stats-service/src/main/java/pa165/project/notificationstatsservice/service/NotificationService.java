package pa165.project.notificationstatsservice.service;

import pa165.project.notificationstatsservice.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications();
    Notification getById(Long id);
    Notification createNotification(Notification notification);
    void deleteNotification(Long id);
    Notification createOverdueNotification(String recipientEmail, String bookTitle);
    List<Notification> getAllSortedByDate();
}
