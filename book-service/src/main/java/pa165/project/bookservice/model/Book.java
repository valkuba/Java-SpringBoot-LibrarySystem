package pa165.project.bookservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String description;
    private int availableCopies;
    private int yearPublication;
}
