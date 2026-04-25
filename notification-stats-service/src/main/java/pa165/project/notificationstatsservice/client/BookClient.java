package pa165.project.notificationstatsservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class BookClient {

    @Value("${services.book.base-url:http://localhost:8080}")
    private String bookBaseUrl;

    public String resolveBookTitle(Long bookCopyId) {
        if (bookCopyId == null)
            return "Unknown Book";
        try {
            BookLookupResponse book = RestClient.create(bookBaseUrl)
                    .get()
                    .uri("/api/books/{id}", bookCopyId)
                    .retrieve()
                    .body(BookLookupResponse.class);

            if (book == null || book.title() == null || book.title().isBlank()) {
                return "BookCopy #" + bookCopyId;
            }

            return book.title();
        } catch (RestClientException ex) {
            // catch when you cannot connect to the api uri
            log.warn("Book lookup REST failed bookCopyId={} reason={}", bookCopyId, ex.getMessage());
            return "BookCopy #" + bookCopyId;
        } catch (Exception ex) {
            log.warn("Book lookup failed bookCopyId={} reason={}", bookCopyId, ex.getMessage());
            return "BookCopy #" + bookCopyId;
        }
    }

    private record BookLookupResponse(String title) {
    }
}

