package pa165.project.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pa165.project.bookservice.model.Book;
import pa165.project.bookservice.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooks(String title, String author, String genre) {
        return bookRepository.findAll().stream()
                .filter(b -> title == null || b.getTitle().toLowerCase().contains(title)) // null -> ignore filter
                .filter(b -> author == null || b.getAuthor().toLowerCase().contains(author))
                .filter(b -> genre == null || b.getGenre().toLowerCase().contains(genre))
                .collect(Collectors.toList());
    }
}
