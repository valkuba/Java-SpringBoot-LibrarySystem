package pa165.project.notificationstatsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pa165.project.notificationstatsservice.exception.ResourceNotFoundException;
import pa165.project.notificationstatsservice.model.Notification;
import pa165.project.notificationstatsservice.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification with id " + id + " not found"));
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(Notification.NotificationStatus.PENDING);
        return notificationRepository.saveNotification(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

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
