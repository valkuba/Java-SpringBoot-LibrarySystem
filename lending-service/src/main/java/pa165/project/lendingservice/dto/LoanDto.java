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
public class LoanDto {

    @Schema(description = "Unique identifier of the loan")
    @NotNull
    private Long id;

    @Schema(description = "Unique identifier of member")
    @NotNull(message = "memberId is required")
    private Long memberId;

    @Schema(description = "Unique identifier of loaned book copy")
    @NotNull(message = "bookCopyId is required")
    private Long bookCopyId;

    @Schema(description = "Date when the book was loaned")
    private LocalDate loanDate;

    @Schema(description = "Due date for returning the book")
    private LocalDate dueDate;

    @Schema(description = "Date of actual return")
    private LocalDate returnDate;

    @Schema(description = "Loan status (LOANED, RETURNED, LOST, LATE)")
    @NotNull
    private String status;
}