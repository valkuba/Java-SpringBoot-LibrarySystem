package pa165.project.notificationstatsservice.messaging;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEventMessage {
    private Long loanId;
    private Long memberId;
    private Long bookCopyId;
    private NotificationType type;
    private Double fineAmount;
    private String status;
    private String date;
}
