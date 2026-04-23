package pa165.project.notificationstatsservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pa165.project.notificationstatsservice.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationMessageConsumer {
    private final NotificationService notificationService;

    @Value("${services.member.base-url:http://localhost:8082}")
    private String memberBaseUrl;

    @Value("${services.book.base-url:http://localhost:8080}")
    private String bookBaseUrl;

    @JmsListener(destination = "${messaging.queue.loan-events}")
    public void receiveNotification(LoanEventMessage msg) {
        if (msg == null || msg.getType() == null) {
            log.warn("Received invalid JMS message: {}", msg);
            return;
        }

        String recipientEmail = resolveRecipientEmail(msg.getMemberId());
        String bookTitle = resolveBookTitle(msg.getBookCopyId());

        switch (msg.getType()) {
            case LOAN_CREATED -> notificationService.createLoanCreatedNotification(
                    recipientEmail, bookTitle, msg.getLoanId(), msg.getDate());
            case LOAN_RETURNED -> notificationService.createLoanReturnedNotification(
                    recipientEmail, bookTitle, msg.getLoanId(), msg.getDate(), msg.getStatus());
            case FINE_ISSUED -> notificationService.createFineIssuedNotification(
                    recipientEmail, bookTitle, msg.getLoanId(), msg.getFineAmount(), msg.getDate());
            default -> log.warn("Unsupported event type={} for loanId={}", msg.getType(), msg.getLoanId());
        }

        log.info("Received loan event type={} loanId={}", msg.getType(), msg.getLoanId());
    }

    private String resolveRecipientEmail(Long memberId) {
        try {
            MemberLookupResponse member = RestClient.create(memberBaseUrl)
                    .get()
                    .uri("/api/members/{id}", memberId)
                    .retrieve()
                    .body(MemberLookupResponse.class);

            if (member == null || member.email() == null || member.email().isBlank()) {
                return "unknown-email-for-member-" + memberId;
            }
            return member.email();
        } catch (Exception ex) {
            log.warn("Member lookup failed memberId={} reason={}", memberId, ex.getMessage());
            return "unknown-email-for-member-" + memberId;
        }
    }

    private String resolveBookTitle(Long bookCopyId) {
        if (bookCopyId == null) {
            return "Unknown book";
        }

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
        } catch (Exception ex) {
            log.warn("Book lookup failed bookCopyId={} reason={}", bookCopyId, ex.getMessage());
            return "BookCopy #" + bookCopyId;
        }
    }

    private record MemberLookupResponse(String email) {
    }

    private record BookLookupResponse(String title) {
    }
}
