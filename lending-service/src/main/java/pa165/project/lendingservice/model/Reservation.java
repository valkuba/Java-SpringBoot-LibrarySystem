package pa165.project.lendingservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    private Long id;
    private Long memberId;
    private Long bookId;
    private LocalDate reservationDate;
    private LocalDate whenAvailableDate;
    private String status;
}
