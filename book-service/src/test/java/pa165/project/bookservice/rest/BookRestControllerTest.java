package pa165.project.bookservice.rest;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.bookservice.dto.BookDto;
import pa165.project.bookservice.util.TestDataFactory;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* *
 * Test for the controller layer
 * */

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper method for book creation, so it can be reused in multiple tests
    // Just to keep code a bit cleaner
    private BookDto createBookHelper(BookDto bookDto) throws Exception {
        String response = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, BookDto.class);
    }

    @Test
    void getAllBooks_BooksFound_ReturnsListOfBooks() throws Exception {
        // Only checks response code 200 and valid JSON (returns 0 books, because no book was created)
        String response = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookDto> books = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));
        assertThat("books", books, is(notNullValue()));
    }

    @Test
    void getBookById_BookFound_ReturnsBook() throws Exception {
        // Creates a book before, so it can check it
        BookDto bookDto = TestDataFactory.bookDtos.get(0);
        Long createdId = createBookHelper(bookDto).getId();

        String response = mockMvc.perform(get("/api/books/{id}", createdId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto foundBook = objectMapper.readValue(response, BookDto.class);
        assertThat("title", foundBook.getTitle(), is(equalTo(bookDto.getTitle())));
        assertThat("author", foundBook.getAuthor(), is(equalTo(bookDto.getAuthor())));
    }

    @Test
    void getBookById_BookNotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/books/676767"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_BookCreated_ReturnsBook() throws Exception {
        BookDto bookDto = TestDataFactory.bookDtos.get(0);

        BookDto createdBook = createBookHelper(bookDto);

        assertThat("title", createdBook.getTitle(), is(equalTo(bookDto.getTitle())));
        assertThat("author", createdBook.getAuthor(), is(equalTo(bookDto.getAuthor())));
    }

    @Test
    void createBook_InvalidIsbn_ReturnsBadRequest() throws Exception {
        BookDto bookDto = TestDataFactory.bookDtos.get(0);
        BookDto invalidBookDto = BookDto.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .isbn("NotISBNForSure")
                .genre(bookDto.getGenre())
                .description(bookDto.getDescription())
                .availableCopies(bookDto.getAvailableCopies())
                .yearPublication(bookDto.getYearPublication())
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_BookUpdated_ReturnsBook() throws Exception {
        BookDto createdBook = createBookHelper(TestDataFactory.bookDtos.get(0));
        BookDto updatedBookDto = TestDataFactory.bookDtos.get(1);

        String response = mockMvc.perform(put("/api/books/{id}", createdBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBookDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto updatedBook = objectMapper.readValue(response, BookDto.class);
        assertThat("title", updatedBook.getTitle(), is(equalTo(updatedBookDto.getTitle())));
        assertThat("author", updatedBook.getAuthor(), is(equalTo(updatedBookDto.getAuthor())));
    }

    @Test
    void updateBook_BookNotFound_Returns404() throws Exception {
        mockMvc.perform(put("/api/books/676767")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestDataFactory.bookDtos.get(0))))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_BookDeleted_ReturnsNoContent() throws Exception {
        BookDto createdBook = createBookHelper(TestDataFactory.bookDtos.get(0));

        mockMvc.perform(delete("/api/books/{id}", createdBook.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchBooks_ByTitle_ReturnsListOfBooks() throws Exception {
        BookDto createdBook = createBookHelper(TestDataFactory.bookDtos.get(0));
        String title = createdBook.getTitle();

        String response = mockMvc.perform(get("/api/books/search")
                        .param("title", title))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookDto> books = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));
        assertThat("books size", books.size(), is(greaterThan(0)));
        assertThat("title", books.get(0).getTitle(), is(equalTo(title)));
    }

    @Test
    void searchBooks_NoResults_ReturnsEmptyList() throws Exception {
        String response = mockMvc.perform(get("/api/books/search")
                        .param("title", "TitleThatIJustMadeUp123"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookDto> books = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));
        assertThat("books", books, is(empty()));
    }
}
