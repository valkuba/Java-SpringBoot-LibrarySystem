package pa165.project.bookservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.bookservice.model.Book;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class BookRepositoryImpl implements BookRepository {

    // we dont use JPA in the M1 so instead use HashMap
    private final Map<Long, Book> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Book save(Book book) {
        if(book.getId() == null) {
            book.setId(idGenerator.getAndIncrement());
        }
        store.put(book.getId(), book);
        return book;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return store.values().stream() // values() - get all values from the HashMap, stream() - create stream from it
                .filter(b -> b.getAuthor().equalsIgnoreCase(author)) // for each book b, take only those where author is author
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitle(String title) {
        return store.values().stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return store.values().stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
}
