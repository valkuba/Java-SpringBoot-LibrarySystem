package pa165.project.notificationstatsservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookServiceClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${services.book.base-url:http://localhost:8081}")
    private String baseUrl;

    @Value("${services.book.title-by-loan-path:/api/books/notification-context/{loanId}}")
    private String titleByLoanPath;

    @Value("${services.book.title-by-copy-path:/api/books/copies/{bookCopyId}}")
    private String titleByCopyPath;

    public Optional<String> getBookTitle(Long loanId, Long bookCopyId) {
        RestClient client = restClientBuilder.baseUrl(baseUrl).build();

        Optional<String> byLoan = tryResolveTitle(client, titleByLoanPath, loanId);
        if (byLoan.isPresent()) {
            return byLoan;
        }

        return tryResolveTitle(client, titleByCopyPath, bookCopyId);
    }

    private Optional<String> tryResolveTitle(RestClient client, String path, Long variable) {
        if (variable == null) {
            return Optional.empty();
        }

        try {
            Map response = client.get()
                    .uri(path, variable)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return Optional.empty();
            }

            Object title = response.get("bookTitle");
            if (title == null) {
                title = response.get("title");
            }

            return Optional.ofNullable(title).map(String::valueOf);
        } catch (Exception ex) {
            log.warn("Cannot resolve book title using path={} variable={}: {}", path, variable, ex.getMessage());
            return Optional.empty();
        }
    }
}

