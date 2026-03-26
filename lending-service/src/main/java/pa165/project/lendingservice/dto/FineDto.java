package pa165.project.lendingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Loan data transfer object")
public class FineDto {

    @Schema(description = "Unique identifier of the fine")
    private Long id;

    @Schema(description = "Unique identifier of member")
    @NotNull(message = "memberId is required")
    private Long memberId;

    @Schema(description = "Unique identifier of fined loan")
    @NotNull(message = "loanId is required")
    private Long loanId;

    @Schema(description = "Date when the book was returned")
    private LocalDate date;

    @Schema(description = "Fine amount")
    private Double amount;

    @Schema(description = "Fine Status (PAID, UNPAID)")
    private String status;
}
