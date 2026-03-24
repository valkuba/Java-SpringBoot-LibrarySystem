package pa165.project.bookservice.repository;

import pa165.project.bookservice.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository
{
    List<Book> findAll();
    Optional<Book> findById(Long id);
    Book save(Book book);
    void deleteById(Long id);
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByGenre(String genre);
}
