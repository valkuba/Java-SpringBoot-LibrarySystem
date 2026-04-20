package pa165.project.notificationstatsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/* *
 * One dto for async communication, the type decides what is needed to be passed
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingNotificationDto {

    public enum NotificationType {
        OVERDUE,
        FINE_ISSUED,
        RESERVATION_READY
    }

    private NotificationType type;

    private String recipientEmail;

    private String bookTitle;

    private LocalDate dueDate;

    private Double fineAmount;

    private LocalDate whenAvailableDate;
}