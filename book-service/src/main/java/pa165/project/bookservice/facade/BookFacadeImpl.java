package pa165.project.bookservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.mappers.BookMapper;
import pa165.project.bookservice.model.Book;
import pa165.project.bookservice.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookFacadeImpl implements BookFacade {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getAllBooks() {
        return  bookService.getAllBooks().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        return bookService.getBookById(id)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        return  bookMapper.toDto(bookService.createBook(bookMapper.toModel(bookDto)));
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        return bookMapper.toDto(bookService.updateBook(id, bookMapper.toModel(bookDto)));
    }

    @Override
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    @Override
    public List<BookDto> searchBooks(String title, String author, String genre) {
        return bookService.searchBooks(title, author, genre).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

}
