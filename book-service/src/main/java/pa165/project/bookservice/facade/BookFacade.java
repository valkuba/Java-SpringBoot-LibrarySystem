package pa165.project.bookservice.facade;

import pa165.project.bookservice.dto.BookDto;
import java.util.List;
import java.util.Optional;

public interface BookFacade {
    List<BookDto> getAllBooks();
    Optional<BookDto> getBookById(Long id);
    BookDto createBook(BookDto book);
    BookDto updateBook(Long id, BookDto book);
    void deleteBook(Long id);
    List<BookDto> searchBooks(String title, String author, String genre);
}
