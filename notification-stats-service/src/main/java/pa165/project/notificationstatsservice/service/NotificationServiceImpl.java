package pa165.project.notificationstatsservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pa165.project.notificationstatsservice.model.Notification;
import pa165.project.notificationstatsservice.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/* *
 * Implementation of the Service methods defined by interface.
 * */

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final MeterRegistry meterRegistry;

    private Counter notificationCreatedCounter;

    @PostConstruct
    public void init() {
        notificationCreatedCounter = Counter.builder("notifications.created.total")
                .description("Total number of notifications created")
                .register(meterRegistry);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    @Transactional
    @Override
    public Notification createNotification(Notification notification) {
        notificationCreatedCounter.increment();

        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Transactional
    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Notification createOverdueNotification(String recipientEmail, String bookTitle) {
        Notification n = new Notification();
        n.setMessage("Your borrowed book '" + bookTitle + "' is overdue.");
        n.setRecipientEmail(recipientEmail);
        return createNotification(n);
    }

    @Transactional
    @Override
    public Notification createLoanCreatedNotification(String recipientEmail, String bookTitle, Long loanId, String loanDate) {
        String message = "Loan " + loanId + " for '" + bookTitle + "' was created on " + loanDate + ".";
        return createFromMessage(recipientEmail, message);
    }

    @Transactional
    @Override
    public Notification createLoanReturnedNotification(String recipientEmail, Long loanId, String bookTitle, String returnDate, String status) {
        String message = "Loan " + loanId + " for book '" + bookTitle + "' was returned on " + returnDate + ". Status: " + status + ".";
        return createFromMessage(recipientEmail, message);
    }

    @Transactional
    @Override
    public Notification createFineIssuedNotification(String recipientEmail, Long loanId, String bookTitle, Double fineAmount, String date) {
        String message = "Fine of " + fineAmount + " CZK was issued for loan " + loanId + " ('" + bookTitle + "')" + " on " + date + ".";
        return createFromMessage(recipientEmail, message);
    }

    @Transactional
    @Override
    public Notification createReservationCreatedNotification(String recipientEmail, String bookTitle, Long reservationId,
                                                            String reservationDate, String whenAvailableDate, String status) {
        String message = "Reservation " + reservationId + " for '" + bookTitle + "' was created on " + reservationDate
                + ". Available on: " + whenAvailableDate + ". Status: " + status + ".";
        return createFromMessage(recipientEmail, message);
    }

    @Transactional
    @Override
    public Notification updateReservationCreatedNotification(String recipientEmail, String bookTitle, Long reservationId,
                                                             String reservationDate, String whenAvailableDate, String status) {
        String message = "Reservation " + reservationId + " for '" + bookTitle + "' was updated on " + reservationDate
                + ". Available on: " + whenAvailableDate + ". Status: " + status + ".";
        return createFromMessage(recipientEmail, message);
    }


    private Notification createFromMessage(String recipientEmail, String message) {
        notificationCreatedCounter.increment();

        Notification notification = new Notification();
        notification.setRecipientEmail(recipientEmail);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllSortedByDate() {
        return notificationRepository.findAll().stream()
                .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
                .toList();
    }
}
