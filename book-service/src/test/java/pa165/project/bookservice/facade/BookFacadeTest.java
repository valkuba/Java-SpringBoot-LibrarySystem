package pa165.project.bookservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.mappers.BookMapper;
import pa165.project.bookservice.service.BookServiceImpl;
import pa165.project.bookservice.util.TestDataFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

/* *
 * Tests for facade layer
 */

@ExtendWith(MockitoExtension.class)
public class BookFacadeTest {
    @Mock
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookFacadeImpl bookFacade;

    @Test
    void getAllBooks_BooksFound_ReturnsListOfBooks() {
        Mockito.when(bookService.getAllBooks())
                .thenReturn(TestDataFactory.bookEntities);
        Mockito.when(bookMapper.toDto(any()))
                .thenReturn(TestDataFactory.bookDtos.get(0));

        BookDto foundEntity = bookFacade.getAllBooks().get(0);

        assertThat(foundEntity).isEqualTo(TestDataFactory.bookDtos.get(0));
    }

    @Test
    void getBookById_BookFound_ReturnsBook() {
        Mockito.when(bookService.getBookById(1L))
                .thenReturn(Optional.ofNullable(TestDataFactory.bookEntities.get(0)));
        Mockito.when(bookMapper.toDto(any()))
                .thenReturn(TestDataFactory.bookDtos.get(0));

        BookDto foundEntity = bookFacade.getBookById(1L).orElseThrow();

        assertThat(foundEntity).isEqualTo(TestDataFactory.bookDtos.get(0));
    }

    @Test
    void getBookById_BookNotFound_ReturnsEmpty() {
        Mockito.when(bookService.getBookById(99L))
                .thenReturn(Optional.empty());

        Optional<BookDto> foundEntity = bookFacade.getBookById(99L);

        assertTrue(foundEntity.isEmpty());
    }

    @Test
    void createBook_BookCreated_ReturnsBook() {
        Mockito.when(bookMapper.toModel(any()))
                .thenReturn(TestDataFactory.bookEntities.get(0));
        Mockito.when(bookService.createBook(any()))
                .thenReturn(TestDataFactory.bookEntities.get(0));
        Mockito.when(bookMapper.toDto(any()))
                .thenReturn(TestDataFactory.bookDtos.get(0));

        BookDto foundEntity = bookFacade.createBook(TestDataFactory.bookDtos.get(0));

        assertEquals(TestDataFactory.bookDtos.get(0), foundEntity);
        Mockito.verify(bookService).createBook(TestDataFactory.bookEntities.get(0));
    }

    @Test
    void updateBook_BookUpdated_ReturnsBook() {
        Mockito.when(bookMapper.toModel(any()))
                .thenReturn(TestDataFactory.bookEntities.get(0));
        Mockito.when(bookService.updateBook(any(), any()))
                .thenReturn(TestDataFactory.bookEntities.get(0));
        Mockito.when(bookMapper.toDto(any()))
                .thenReturn(TestDataFactory.bookDtos.get(0));

        BookDto foundEntity = bookFacade.updateBook(2L, TestDataFactory.bookDtos.get(0));

        assertEquals(TestDataFactory.bookDtos.get(0), foundEntity);
        Mockito.verify(bookService).updateBook(2L, TestDataFactory.bookEntities.get(0));
    }

    @Test
    void deleteBook_BookDeleted_ReturnsNothing() {
        bookFacade.deleteBook(1L);

        Mockito.verify(bookService).deleteBook(1L);
    }

    @Test
    void searchBooks_BookFoundByGenre_ReturnsListOfBooks() {
        Mockito.when(bookService.searchBooks(null, null, TestDataFactory.bookEntities.get(0).getGenre()))
                .thenReturn(Collections.singletonList(TestDataFactory.bookEntities.get(0)));
        Mockito.when(bookMapper.toDto(any()))
                .thenReturn(TestDataFactory.bookDtos.get(0));

        List<BookDto> foundEntities = bookFacade.searchBooks(null, null, TestDataFactory.bookEntities.get(0).getGenre());

        assertThat(foundEntities).isEqualTo(Collections.singletonList(TestDataFactory.bookDtos.get(0)));
    }

    @Test
    void searchBooks_BookNotFoundByGenre_ReturnsEmptyList() {
        Mockito.when(bookService.searchBooks(null, null, "NonExistingGenre"))
                .thenReturn(Collections.emptyList());

        List<BookDto> foundEntities  = bookFacade.searchBooks(null, null, "NonExistingGenre");

        assertThat(foundEntities).isEmpty();
    }
}
