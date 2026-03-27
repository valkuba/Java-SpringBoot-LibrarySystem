package pa165.project.bookservice.util;

import org.springframework.stereotype.Component;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.model.Book;

import java.util.List;

/* *
 * Data for tests of each layer
 * Use bookDto or BookEntities in the tests
 */

@Component
public class TestDataFactory {

    public static List<BookDto> bookDtos = getBookDtosFactory();
    public static List<Book> bookEntities = getBookEntitiesFactory();

    private static List<BookDto> getBookDtosFactory() {
        BookDto bookDto1 = BookDto.builder()
                .id(1L)
                .title("Fight Club")
                .author("Chuck Palahniuk")
                .isbn("9780091835132")
                .genre("Novel")
                .description("Lorem ipsum")
                .availableCopies(7)
                .yearPublication(1999)
                .build();

        BookDto bookDto2 = BookDto.builder()
                .id(2L)
                .title("Great Gatsby")
                .author("Francis Scott Fitzgerald")
                .isbn("9781853260414")
                .genre("Novel")
                .description("Lorem ipsum 2")
                .availableCopies(1)
                .yearPublication(1925)
                .build();

        return List.of(bookDto1, bookDto2);
    }

    private static List<Book> getBookEntitiesFactory() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Fight Club");
        book1.setAuthor("Chuck Palahniuk");
        book1.setIsbn("9780091835132");
        book1.setGenre("Novel");
        book1.setDescription("Lorem ipsum");
        book1.setAvailableCopies(7);
        book1.setYearPublication(1999);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Great Gatsby");
        book2.setAuthor("Francis Scott Fitzgerald");
        book2.setIsbn("9781853260414");
        book2.setGenre("Novel");
        book2.setDescription("Lorem ipsum 2");
        book2.setAvailableCopies(1);
        book2.setYearPublication(1925);

        return List.of(book1, book2);
    }
}
