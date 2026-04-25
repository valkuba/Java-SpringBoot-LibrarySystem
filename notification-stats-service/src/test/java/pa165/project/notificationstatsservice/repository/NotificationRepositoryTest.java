package pa165.project.notificationstatsservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pa165.project.notificationstatsservice.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();

        notificationRepository.save(Notification.builder()
                .message("Your book is overdue.")
                .recipientEmail("mamradchleba@centrum.cz")
                .createdAt(LocalDateTime.now().minusDays(3))
                .build());

        notificationRepository.save(Notification.builder()
                .message("Your reservation is ready.")
                .recipientEmail("mamradchleba@centrum.cz")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build());

        notificationRepository.save(Notification.builder()
                .message("Your book is overdue.")
                .recipientEmail("grey256@dorian.om")
                .createdAt(LocalDateTime.now().minusDays(2))
                .build());
    }

    @Test
    void findByRecipientEmail_EmailExists_ReturnsAllMatchingNotifications() {
        List<Notification> result = notificationRepository.findByRecipientEmail("mamradchleba@centrum.cz");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Notification::getRecipientEmail).containsOnly("mamradchleba@centrum.cz");
    }

    @Test
    void findByRecipientEmail_EmailNotExists_ReturnsEmptyList() {
        List<Notification> result = notificationRepository.findByRecipientEmail("nobody@listens.cz");

        assertThat(result).isEmpty();
    }

    @Test
    void findAllByOrderByCreatedAtDesc_ReturnsNotificationsInCorrectOrder() {
        List<Notification> result = notificationRepository.findAllByOrderByCreatedAtDesc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getCreatedAt())
                .isAfterOrEqualTo(result.get(1).getCreatedAt());
        assertThat(result.get(1).getCreatedAt())
                .isAfterOrEqualTo(result.get(2).getCreatedAt());
    }

    @Test
    void save_ValidNotification_PersistsWithGeneratedId() {
        Notification saved = notificationRepository.save(Notification.builder()
                .message("New notification.")
                .recipientEmail("donotreplay@gmail.com")
                .createdAt(LocalDateTime.now())
                .build());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMessage()).isEqualTo("New notification.");
    }
}
