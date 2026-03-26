package pa165.project.bookservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.bookservice.model.Book;
import pa165.project.bookservice.repository.BookRepositoryImpl;
import pa165.project.bookservice.util.TestDataFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepositoryImpl bookRepository;
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
        Mockito.when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Book> foundEntity = bookService.getBookById(99L);

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
    void updateBook_BookUpdated_ReturnsBook() {
        Mockito.when(bookRepository.save(TestDataFactory.bookEntities.get(0)))
                .thenReturn(TestDataFactory.bookEntities.get(0));

        Book result = bookService.updateBook(1L, TestDataFactory.bookEntities.get(0));

        assertEquals(1L, result.getId());
        Mockito.verify(bookRepository).save(TestDataFactory.bookEntities.get(0));
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

        List<Book> result = bookService.searchBooks(null, TestDataFactory.bookEntities.get(0).getAuthor(), null);

        assertThat(result).containsExactly(TestDataFactory.bookEntities.get(0));
    }

    @Test
    void searchBooks_BookNotFoundByAuthor_ReturnsEmpty() {
        Mockito.when(bookRepository.findAll())
                .thenReturn(TestDataFactory.bookEntities);

        List<Book> result = bookService.searchBooks(null, "Not the author", null);

        assertThat(result).isEmpty();
    }


}
