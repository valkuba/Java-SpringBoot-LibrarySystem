package pa165.project.notificationstatsservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/* *
 * Class for Model of the notification
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private Long id;
    private String message;
    private String recipientEmail;
    private LocalDateTime createdAt;
}
