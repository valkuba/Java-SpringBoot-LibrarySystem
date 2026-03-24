package pa165.project.notificationstatsservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.notificationstatsservice.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/* *
 * Implementation of the Repository methods defined by interface.
 * */
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final HashMap<Long, Notification> notifications = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notifications.values());
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(notifications.get(id));

    }

    @Override
    public Notification saveNotification(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGen.getAndIncrement());
        }
        notifications.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public void deleteById(Long id) {
        notifications.remove(id);

    }

    @Override
    public List<Notification> findByRecipientEmail(String email) {
        return notifications.values().stream()
            .filter(n -> n.getRecipientEmail().equals(email))
            .collect(Collectors.toList());
    }
}
