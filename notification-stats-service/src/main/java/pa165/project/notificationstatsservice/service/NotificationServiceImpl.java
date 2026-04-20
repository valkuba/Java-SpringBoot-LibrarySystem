package pa165.project.notificationstatsservice.service;

import lombok.RequiredArgsConstructor;
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

    @Override
    public List<Notification> getAllSortedByDate() {
        return notificationRepository.findAll().stream()
                .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
                .toList();
    }
}
