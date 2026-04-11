package pa165.project.bookservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter // instead of @Data which generates hashCode/equals based on all fields
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String genre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int availableCopies;

    @Column(nullable = false)
    private int yearPublication;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @PrePersist // automatic date creation
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
}
