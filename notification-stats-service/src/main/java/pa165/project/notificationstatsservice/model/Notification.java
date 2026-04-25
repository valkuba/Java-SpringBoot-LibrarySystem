package pa165.project.notificationstatsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/* *
 * Class for Model of the notification
 * */
@Entity
@Table(name="notification")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String recipientEmail;

    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Notification that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
