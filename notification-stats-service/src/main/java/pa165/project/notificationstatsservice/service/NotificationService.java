package pa165.project.notificationstatsservice.service;

import pa165.project.notificationstatsservice.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> getAllNotifications();
    Optional<Notification> getById(Long id);
    Notification createNotification(Notification notification);
    void deleteNotification(Long id);
    Notification createOverdueNotification(String recipientEmail, String bookTitle);
    Notification createLoanCreatedNotification(String recipientEmail, String bookTitle, Long loanId, String loanDate);
    Notification createLoanReturnedNotification(String recipientEmail, String bookTitle, Long loanId, String returnDate, String status);
    Notification createFineIssuedNotification(String recipientEmail, String bookTitle, Long loanId, Double fineAmount, String date);
    List<Notification> getAllSortedByDate();
}
