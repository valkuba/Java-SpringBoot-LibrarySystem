package pa165.project.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Book data transfer object")
public class BookDto {
    @Schema(description = "Unique identifier of the book")
    private Long id;

    @Schema(description = "Title of the book")
    private String title;

    @Schema(description = "Author of the book")
    private String author;

    @Schema(description = "ISBN of the book")
    private String isbn;

    @Schema(description = "Genre of the book")
    private String genre;

    @Schema(description = "Description of the book")
    private String description;

    @Schema(description = "Number of available copies")
    private int availableCopies;

    @Schema(description = "Year of publication")
    private int yearPublication;
}
