package pa165.project.bookservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pa165.project.bookservice.model.Book;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        bookRepository.save(Book.builder()
            .title("Blood of Elves")
            .author("Andrzej Sapkowski")
            .isbn("9780132350884")
            .genre("Fantasy")
            .description("A book about witcher.")
            .availableCopies(5)
            .yearPublication(2001)
            .build());

        bookRepository.save(Book.builder()
            .title("Baptism of Fire")
            .author("Andrzej Sapkowski")
            .isbn("9780135957059")
            .genre("Fantasy")
            .description("A book about witcher 2.")
            .availableCopies(3)
            .yearPublication(2002)
            .build());

        bookRepository.save(Book.builder()
            .title("Java in Enterprise")
            .author("David Thomas")
            .isbn("9780201633610")
            .genre("Programming")
            .description("A book about Java in Enterprise.")
            .availableCopies(2)
            .yearPublication(2015)
            .build());
    }

    @Test
    void findByAuthor_AuthorExists_ReturnsBooks() {
        List<Book> books = bookRepository.findByAuthor("Andrzej Sapkowski");
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Blood of Elves", "Baptism of Fire");
    }

    @Test
    void findByAuthor_AuthorNotExists_ReturnsEmptyList() {
        List<Book> books = bookRepository.findByAuthor("Andrzej Sapkovski");
        assertThat(books).isEmpty();
    }

    @Test
    void findByGenre_GenreExists_ReturnsBooks() {
        List<Book> books = bookRepository.findByGenre("Fantasy");
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getAuthor).containsOnly("Andrzej Sapkowski");
        assertThat(books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Blood of Elves", "Baptism of Fire");
    }

    @Test
    void findByGenre_GenreNotExists_ReturnsEmptyList() {
        List<Book> books = bookRepository.findByGenre("Sci-fi");
        assertThat(books).isEmpty();
    }

    @Test
    void findByTitle_TitleContains_ReturnsBooks() {
        List<Book> books = bookRepository.findByTitle("Blood");
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("Blood of Elves");
    }

    @Test
    void findByTitle_CaseInsensitive_ReturnsBooks() {
        List<Book> books = bookRepository.findByTitle("blood");
        assertThat(books).isNotEmpty();
    }

    @Test
    void findByTitle_TitleNotExists_ReturnsEmptyList() {
        List<Book> books = bookRepository.findByTitle("Dynamic programming");
        assertThat(books).isEmpty();
    }

    @Test
    void findByTitle_PartialMatch_ReturnsSingleBook() {
        List<Book> books = bookRepository.findByTitle("Blood");
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Blood of Elves");
    }

    @Test
    void findByTitle_MatchesOnlyProgrammingBook() {
        List<Book> books = bookRepository.findByTitle("Enterprise");
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Java in Enterprise");
    }
}
