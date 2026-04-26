package pa165.project.notificationstatsservice.messaging;

import jakarta.jms.JMSException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.client.BookClient;
import pa165.project.notificationstatsservice.client.MemberClient;
import pa165.project.notificationstatsservice.service.NotificationService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationMessageConsumerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private MemberClient memberClient;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private NotificationMessageConsumer consumer;

    @Test
    void receiveNotification_loanCreated_callsCreateLoanCreatedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(1L)
                .memberId(1L)
                .bookCopyId(1L)
                .type(NotificationType.LOAN_CREATED)
                .date("2026-04-24")
                .build();

        when(memberClient.resolveRecipientEmail(1L)).thenReturn("ilove@books.com");
        when(bookClient.resolveBookTitle(1L)).thenReturn("Animal Farm");

        consumer.receiveNotification(message);

        verify(notificationService).createLoanCreatedNotification(
                eq("ilove@books.com"), eq("Animal Farm"), eq(1L), eq("2026-04-24")
        );
        verify(notificationService, never()).createLoanReturnedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createFineIssuedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyDouble(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void receiveNotification_loanReturned_callsCreateLoanReturnedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(11L)
                .memberId(11L)
                .bookCopyId(11L)
                .type(NotificationType.LOAN_RETURNED)
                .status("RETURNED")
                .date("2026-04-24")
                .build();

        when(memberClient.resolveRecipientEmail(11L)).thenReturn("returned@iswear.com");
        when(bookClient.resolveBookTitle(11L)).thenReturn("Unknown Book");

        consumer.receiveNotification(message);

        verify(notificationService).createLoanReturnedNotification(
                eq("returned@iswear.com"), eq(11L), eq("2026-04-24"), eq("RETURNED")
        );
    }

    @Test
    void receiveNotification_fineIssued_callsCreateFineIssuedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(12L)
                .memberId(22L)
                .bookCopyId(32L)
                .type(NotificationType.FINE_ISSUED)
                .fineAmount(150.0)
                .date("2026-04-24")
                .build();

        when(memberClient.resolveRecipientEmail(22L)).thenReturn("joseph@fined.com");
        when(bookClient.resolveBookTitle(32L)).thenReturn("Unknown Book");

        consumer.receiveNotification(message);

        verify(notificationService).createFineIssuedNotification(
                eq("joseph@fined.com"), eq(12L), eq(150.0), eq("2026-04-24")
        );
    }

    @Test
    void receiveNotification_reservationCreated_callsCreateReservationCreatedNotification() throws JMSException {
        ReservationEventMessage message = ReservationEventMessage.builder()
                .reservationId(50L)
                .memberId(70L)
                .bookCopyId(80L)
                .type(NotificationType.RESERVATION_CREATED)
                .date("2026-04-25")
                .whenAvailableDate(LocalDate.parse("2026-04-28"))
                .status("ACTIVE")
                .build();

        when(memberClient.resolveRecipientEmail(70L)).thenReturn("reservation@books.com");
        when(bookClient.resolveBookTitle(80L)).thenReturn("Terrible Train Phantom");

        consumer.receiveNotification(message);

        verify(notificationService).createReservationCreatedNotification(eq("reservation@books.com"), eq("Terrible Train Phantom"), eq(50L), eq("2026-04-25"), eq("2026-04-28"), eq("ACTIVE")
        );
    }
}
