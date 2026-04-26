package pa165.project.lendingservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LendingMessageProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${messaging.queue.notification-events}")
    private String queueName;

    public void sendReservationCreated(Long reservationId, Long memberId, Long bookCopyId, String reservationDate) {
        var message = ReservationEventMessage.builder()
                .reservationId(reservationId)
                .memberId(memberId)
                .bookCopyId(bookCopyId)
                .type(NotificationType.RESERVATION_CREATED)
                .date(reservationDate)
                .build();
        send(message);
    }

    public void sendReservationUpdated(Long reservationId, Long memberId, Long bookCopyId, String status, String reservationDate) {
        var message = ReservationEventMessage.builder()
                .reservationId(reservationId)
                .memberId(memberId)
                .bookCopyId(bookCopyId)
                .type(NotificationType.RESERVATION_UPDATED)
                .status(status)
                .date(reservationDate)
                .build();
        send(message);
    }


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

    private void send(ReservationEventMessage message) {
        log.info("Sending JMS message: {}", message);
        jmsTemplate.convertAndSend(queueName, message);
    }

    private void send(LoanEventMessage message) {
        log.info("Sending JMS message: {}", message);
        jmsTemplate.convertAndSend(queueName, message);
    }
}