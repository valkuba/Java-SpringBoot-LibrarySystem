package pa165.project.notificationstatsservice.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pa165.project.notificationstatsservice.client.BookClient;
import pa165.project.notificationstatsservice.client.MemberClient;
import pa165.project.notificationstatsservice.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationMessageConsumer {
    @Autowired
    private MessageConverter messageConverter;

    private final NotificationService notificationService;
    private final MemberClient memberClient;
    private final BookClient bookClient;

    @JmsListener(destination = "${messaging.queue.notification-events}")
    @Transactional
    public void receiveNotification(Object rawMsg) throws JMSException {
        Object converted = messageConverter.fromMessage((Message) rawMsg);
        if (converted instanceof LoanEventMessage msg) {
            handleLoanEvent(msg);
        } else if (converted instanceof ReservationEventMessage msg) {
            handleReservationEvent(msg);
        } else {
            log.warn("Received unknown message type: {}", rawMsg);
        }
    }

    private void handleLoanEvent(LoanEventMessage msg) {
        String recipientEmail = memberClient.resolveRecipientEmail(msg.getMemberId());
        String bookTitle = bookClient.resolveBookTitle(msg.getBookCopyId());

        switch (msg.getType()) {
            case LOAN_CREATED -> notificationService.createLoanCreatedNotification(
                    recipientEmail, bookTitle, msg.getLoanId(), msg.getDate());
            case LOAN_RETURNED -> notificationService.createLoanReturnedNotification(
                    recipientEmail, msg.getLoanId(), msg.getDate(), msg.getStatus());
            case FINE_ISSUED -> notificationService.createFineIssuedNotification(
                    recipientEmail, msg.getLoanId(), msg.getFineAmount(), msg.getDate());
            default -> log.warn("Unsupported event type={} for loanId={}", msg.getType(), msg.getLoanId());
        }

        log.info("Received loan event type={} loanId={}", msg.getType(), msg.getLoanId());
    }

    private void handleReservationEvent(ReservationEventMessage msg) {
        String recipientEmail = memberClient.resolveRecipientEmail(msg.getMemberId());
        String bookTitle = bookClient.resolveBookTitle(msg.getBookCopyId());

        switch (msg.getType()) {
            case RESERVATION_CREATED -> notificationService.createReservationCreatedNotification(
                    recipientEmail, bookTitle, msg.getReservationId(), msg.getDate(), String.valueOf(msg.getWhenAvailableDate()), msg.getStatus());
            case RESERVATION_UPDATED -> notificationService.updateReservationCreatedNotification(
                    recipientEmail, bookTitle, msg.getReservationId(), msg.getDate(), String.valueOf(msg.getWhenAvailableDate()), msg.getStatus());
            default -> log.warn("Unsupported event type={} for reservationId={}", msg.getType(), msg.getReservationId());
        }

        log.info("Received reservation event type={} loanId={}", msg.getType(), msg.getReservationId());
    }
}
