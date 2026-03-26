package pa165.project.bookservice.service;

import pa165.project.bookservice.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    List<Book> searchBooks(String title, String author, String genre);
}
