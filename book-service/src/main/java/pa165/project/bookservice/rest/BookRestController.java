package pa165.project.bookservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.facade.BookFacade;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book management API")
public class BookRestController {

    private final BookFacade bookFacade;

    @GetMapping
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookFacade.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookFacade.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookFacade.createBook(bookDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookFacade.updateBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookFacade.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title, author and/or genre")
    public ResponseEntity<List<BookDto>>  searchBooks(@RequestParam(required = false) String title,
                                                      @RequestParam(required = false) String author,
                                                      @RequestParam(required = false) String genre) {
        return  ResponseEntity.ok(bookFacade.searchBooks(title, author, genre));
    }

    @ExceptionHandler(NoSuchElementException.class) // handle not found exception when book is not found
    public ResponseEntity<String> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class) // handle bad request exception when ISBN is invalid
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
