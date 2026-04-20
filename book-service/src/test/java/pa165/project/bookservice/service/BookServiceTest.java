package pa165.project.bookservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.bookservice.model.Book;
import pa165.project.bookservice.repository.BookRepository;
import pa165.project.bookservice.util.TestDataFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/* *
 * Test for the service layer
 * */

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_BooksFound_ReturnsListOfBooks() {
        Mockito.when(bookRepository.findAll())
                .thenReturn(TestDataFactory.bookEntities);

        List<Book> foundEntities = bookService.getAllBooks();

        assertThat(foundEntities).isEqualTo(TestDataFactory.bookEntities);
    }

    @Test
    void getBookById_BookFound_ReturnsBook() {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(Optional.ofNullable(TestDataFactory.bookEntities.get(0)));

        Book foundEntity = bookService.getBookById(1L).orElseThrow();

        assertThat(foundEntity).isEqualTo(TestDataFactory.bookEntities.get(0));
    }

    @Test
    void getBookById_BookNotFound_ReturnsEmpty() {
        Mockito.when(bookRepository.findById(67676767L))
                .thenReturn(Optional.empty());

        Optional<Book> foundEntity = bookService.getBookById(67676767L);

        assertTrue(foundEntity.isEmpty());
    }

    @Test
    void createBook_BookCreated_ReturnsBook() {
        Mockito.when(bookRepository.save(TestDataFactory.bookEntities.get(0)))
                .thenReturn(TestDataFactory.bookEntities.get(0));

        Book foundEntity = bookService.createBook(TestDataFactory.bookEntities.get(0));

        assertEquals(TestDataFactory.bookEntities.get(0), foundEntity);
        Mockito.verify(bookRepository).save(TestDataFactory.bookEntities.get(0));
    }

    @Test
    void createBook_InvalidIsbnWrongCheckDigit_ThrowsException() {
        Book invalidBook = new Book();
        invalidBook.setIsbn("9780060934911");

        // Act + assert together, because assertThrows triggers the exception
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(invalidBook));
    }

    @Test
    void updateBook_BookUpdated_ReturnsBook() {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.bookEntities.get(1)));
        Mockito.when(bookRepository.save(TestDataFactory.bookEntities.get(1)))
                .thenReturn(TestDataFactory.bookEntities.get(1));

        Book foundEntity = bookService.updateBook(1L, TestDataFactory.bookEntities.get(1));

        assertEquals(1L, foundEntity.getId());
        Mockito.verify(bookRepository).save(TestDataFactory.bookEntities.get(1));
    }

    @Test
    void updateBook_InvalidIsbnFormat_ThrowsException() {
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(Optional.of(TestDataFactory.bookEntities.get(0)));

        Book invalidBook = new Book();
        invalidBook.setId(67L);
        invalidBook.setIsbn("DefinitelyNotISBN");

        assertThrows(IllegalArgumentException.class,
                () -> bookService.updateBook(1L, invalidBook));
    }

    @Test
    void updateBook_BookNotFound_ThrowsException() {
        Mockito.when(bookRepository.findById(67676767L))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> bookService.updateBook(67676767L, TestDataFactory.bookEntities.get(0)));
    }

    @Test
    void deleteBook_BookDeleted_ReturnsNothing() {
        bookService.deleteBook(1L);

        Mockito.verify(bookRepository).deleteById(1L);
    }

    @Test
    void searchBooks_BookFoundByAuthor_ReturnsBook() {
        Mockito.when(bookRepository.findAll())
                .thenReturn(TestDataFactory.bookEntities);

        List<Book> foundEntities = bookService.searchBooks(null, TestDataFactory.bookEntities.get(0).getAuthor(), null);

        assertThat(foundEntities).containsExactly(TestDataFactory.bookEntities.get(0));
    }

    @Test
    void searchBooks_BookNotFoundByAuthor_ReturnsEmpty() {
        Mockito.when(bookRepository.findAll())
                .thenReturn(TestDataFactory.bookEntities);

        List<Book> foundEntities = bookService.searchBooks(null, "Not the author", null);

        assertThat(foundEntities).isEmpty();
    }
}
