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
@Schema(description = "Reservation data transfer object")
public class ReservationDto {

    @Schema(description = "Unique identifier of the reservation")
    private Long id;

    @Schema(description = "Unique identifier of the member")
    @NotNull(message = "memberId is required")
    private Long memberId;

    @Schema(description = "Unique identifier of the book")
    @NotNull(message = "bookId is required")
    private Long bookId;

    @Schema(description = "Date when the reservation was made")
    private LocalDate reservationDate;

    @Schema(description = "Date when the book will be available for pickup")
    private LocalDate whenAvailableDate;

    @Schema(description = "Status of the reservation (e.g., ACTIVE, CANCELLED)")
    private String status;
}