package pa165.project.notificationstatsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* *
 * DTO for aggregation of popular books
 * API response, so just for reading
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularBookDto {
    @Schema(description = "Title of the book")
    private String bookTitle;

    @Schema(description = "Number of times the book has been borrowed")
    private Long borrowCount;
}
