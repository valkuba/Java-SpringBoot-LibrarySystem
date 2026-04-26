package pa165.project.notificationstatsservice.messaging;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEventMessage {
    @JsonAlias({"loanId", "id"})
    private Long loanId;
    private Long memberId;
    @JsonAlias({"bookCopyId", "bookId"})
    private Long bookCopyId;
    private NotificationType type;
    private Double fineAmount;
    private String status;
    @JsonAlias({"date", "reservationDate"})
    private String date;
    private String whenAvailableDate;
}
