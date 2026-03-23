package pa165.project.notificationstatsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pa165.project.notificationstatsservice.model.Notification;

import java.time.LocalDateTime;

/* *
 * DTO representing a notification returned by the API.
 * Used as a response object in REST endpoints.
 * Only for reading, so no validation needed.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    @Schema(description = "Unique identifier of the notification")
    private Long id;

    @Schema(description = "Content of the notification message")
    private String message;

    @Schema(description = "Email address of the recipient")
    private String recipientEmail;

    @Schema(description = "Status of the notification", example = "SENT, PENDING, FAILED")
    private Notification.NotificationStatus status;

    @Schema(description = "Timestamp when the notification was created")
    private LocalDateTime createdAt;
}
