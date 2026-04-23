package pa165.project.lendingservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanMessageProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${messaging.queue.loan-events}")
    private String queueName;

    public void sendLoanCreated(Long loanId, Long memberId, Long bookCopyId, String loanDate) {
        var message = LoanEventMessage.builder()
                .loanId(loanId)
                .memberId(memberId)
                .bookCopyId(bookCopyId)
                .type(NotificationType.LOAN_CREATED)
                .date(loanDate)
                .build();
        send(message);
    }

    public void sendLoanReturned(Long loanId, Long memberId, String returnDate) {
        var message = LoanEventMessage.builder()
                .loanId(loanId)
                .memberId(memberId)
                .type(NotificationType.LOAN_RETURNED)
                .status("RETURNED")
                .date(returnDate)
                .build();

        send(message);
    }

    public void sendFineIssued(Long loanId, Long memberId, Double fineAmount, String date) {
        var message = LoanEventMessage.builder()
                .loanId(loanId)
                .memberId(memberId)
                .type(NotificationType.FINE_ISSUED)
                .fineAmount(fineAmount)
                .date(date)
                .build();
        send(message);
    }

    private void send(LoanEventMessage message) {
        log.info("Sending JMS message: {}", message);
        jmsTemplate.convertAndSend(queueName, message);
    }
}