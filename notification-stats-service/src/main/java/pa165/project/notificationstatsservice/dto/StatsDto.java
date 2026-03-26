package pa165.project.notificationstatsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/* *
 * DTO representing a borrowing record used for statistics
 * Contains information about book borrowing transactions
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDto {
    @Schema(description = "Unique identifier of the statistics record")
    private Long id;

    @Schema(description = "Title of the book which stats we want")
    private String bookTitle;

    @Schema(description = "Whether the book has been returned")
    private Boolean returned;
}
