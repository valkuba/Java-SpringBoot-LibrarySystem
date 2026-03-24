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
public class Fine {
    private Long id;
    private Long memberId;
    private Long loanId;
    private LocalDate date;
    private Double amount;
    private String status;
}
