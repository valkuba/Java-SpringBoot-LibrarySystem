package pa165.project.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pa165.project.bookservice.model.Book;
import pa165.project.bookservice.repository.BookRepository;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MeterRegistry meterRegistry;

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    @Override
    public Book createBook(Book book) {
         if (!validateISBN(book.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN: " + book.getIsbn());
        }
         book.setId(null);

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(Long id, Book book) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book with id " + id + " not found"));
        if (!validateISBN(book.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN-13: " + book.getIsbn());
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private boolean validateISBN(String isbn) {
        if(isbn == null) {
            return false;
        }

        isbn = isbn.replace("-", "").replace(" ", "");

        if(isbn.length() != 13) {
            return false;
        }

        if(!isbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for(int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));

            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit == Character.getNumericValue(isbn.charAt(12));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> searchBooks(String title, String author, String genre) {
        if (author != null && !author.isBlank()) {
            meterRegistry.counter("book_searches_by_author", "author_name", author.toLowerCase()).increment();
        }
        
        List<Book> candidates;

        if (title != null) {
            candidates = bookRepository.findByTitle(title);
        } else if (author != null) {
            candidates = bookRepository.findByAuthor(author);
        } else if (genre != null) {
            candidates = bookRepository.findByGenre(genre);
        } else {
            return bookRepository.findAll();
        }

        return candidates.stream()
                .filter(b -> author == null || b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(b -> genre == null || b.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }
}
