package pa165.project.notificationstatsservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pa165.project.notificationstatsservice.client.BookClient;
import pa165.project.notificationstatsservice.client.MemberClient;
import pa165.project.notificationstatsservice.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationMessageConsumer {
    private final NotificationService notificationService;
    private final MemberClient memberClient;
    private final BookClient bookClient;

    @JmsListener(destination = "${messaging.queue.loan-events}")
    public void receiveNotification(LoanEventMessage msg) {
        if (msg == null || msg.getType() == null) {
            log.warn("Received invalid JMS message: {}", msg);
            return;
        }

        String recipientEmail = memberClient.resolveRecipientEmail(msg.getMemberId());
        String bookTitle = bookClient.resolveBookTitle(msg.getBookCopyId());

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
}
