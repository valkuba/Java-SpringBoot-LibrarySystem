package pa165.project.notificationstatsservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class LendingClient {
    @Value("${services.lending.base-url:http://localhost:8081}")
    private String lendingBaseUrl;

    public Long resolveBookId(Long loanId) {
        if (loanId == null)
            return null;
        try {
            LendingClient.LoanLookupResponse loan = RestClient.create(lendingBaseUrl)
                    .get()
                    .uri("/api/loans/{id}", loanId)
                    .retrieve()
                    .body(LendingClient.LoanLookupResponse.class);

            if(loan == null) {
                return null;
            }
            return loan.bookCopyId();
        } catch (RestClientException ex) {
            log.warn("Loan lookup REST failed loanId={} reason={}", loanId, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.warn("Loan lookup failed loanId={} reason={}", loanId, ex.getMessage());
            return null;
        }
    }

    private record LoanLookupResponse(Long bookCopyId) {
    }
}
