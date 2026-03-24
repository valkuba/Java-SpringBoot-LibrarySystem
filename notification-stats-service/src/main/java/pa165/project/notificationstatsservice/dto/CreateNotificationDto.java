package pa165.project.notificationstatsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import io.swagger.v3.oas.annotations.media.Schema;

/* *
 * DTO for creating a new notification.
 * Contains only fields provided by the caller.
 * Fields like id, status, and createdAt are generated server-side.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationDto {
    @Schema(description = "Content of the notification message")
    @NotBlank(message = "Message cannot be blank")
    private String message;

    @Schema(description = "Email address of the recipient")
    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email cannot be blank")
    private String recipientEmail;
}
