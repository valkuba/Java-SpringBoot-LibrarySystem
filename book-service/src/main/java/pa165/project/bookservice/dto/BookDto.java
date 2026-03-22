package pa165.project.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Author of the book")
    @NotBlank(message = "Author is required")
    private String author;

    @Schema(description = "ISBN of the book")
    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Schema(description = "Genre of the book")
    @NotBlank(message = "Genre is required")
    private String genre;

    @Schema(description = "Description of the book")
    private String description;

    @Schema(description = "Number of available copies")
    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;

    @Schema(description = "Year of publication")
    @Min(value = 1000, message = "Year of publication must be valid")
    @Max(value = 2100, message = "Year of publication must be valid")
    private int yearPublication;
}
