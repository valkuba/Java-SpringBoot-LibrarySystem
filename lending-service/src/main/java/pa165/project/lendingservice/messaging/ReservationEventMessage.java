package pa165.project.lendingservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEventMessage {
    private Long reservationId;
    private Long memberId;
    private Long bookCopyId;
    private NotificationType type;
    private String date;
    private LocalDate whenAvailableDate;
    private String status;
}