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
    Notification createLoanReturnedNotification(String recipientEmail, Long loanId, String returnDate, String status);
    Notification createFineIssuedNotification(String recipientEmail, Long loanId, Double fineAmount, String date);
    Notification createReservationCreatedNotification(String recipientEmail, String bookTitle, Long reservationId,
                                                      String reservationDate, String whenAvailableDate, String status);
    Notification updateReservationCreatedNotification(String recipientEmail, String bookTitle, Long reservationId,
                                                      String reservationDate, String whenAvailableDate, String status);
    List<Notification> getAllSortedByDate();
}
