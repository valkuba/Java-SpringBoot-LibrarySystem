package pa165.project.notificationstatsservice.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import pa165.project.notificationstatsservice.client.BookClient;
import pa165.project.notificationstatsservice.client.LendingClient;
import pa165.project.notificationstatsservice.client.MemberClient;
import pa165.project.notificationstatsservice.service.NotificationService;
import pa165.project.notificationstatsservice.service.StatsService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private LendingClient lendingClient;

    @Mock
    private StatsService statsService;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private Message rawMessage;

    @InjectMocks
    private NotificationMessageConsumer consumer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(consumer, "messageConverter", messageConverter);
    }

    @Test
    void receiveNotification_loanCreated_callsCreateLoanCreatedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(1L).memberId(1L).bookCopyId(1L)
                .type(NotificationType.LOAN_CREATED)
                .date("2026-04-24")
                .build();

        when(messageConverter.fromMessage(rawMessage)).thenReturn(message);
        when(memberClient.resolveRecipientEmail(1L)).thenReturn("ilove@books.com");
        when(bookClient.resolveBookTitle(1L)).thenReturn("Animal Farm");

        consumer.receiveNotification(rawMessage);

        verify(notificationService).createLoanCreatedNotification(
                eq("ilove@books.com"), eq("Animal Farm"), eq(1L), eq("2026-04-24")
        );
        verify(notificationService, never()).createLoanReturnedNotification(
                anyString(), anyLong(), anyString(), anyString(), anyString()
        );
        verify(notificationService, never()).createFineIssuedNotification(
                anyString(), anyLong(), anyString(), anyDouble(), anyString()
        );
        verify(statsService).recordLoan(eq(1L), eq("Animal Farm"));
    }

    @Test
    void receiveNotification_loanReturned_callsCreateLoanReturnedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(11L).memberId(11L)
                .type(NotificationType.LOAN_RETURNED)
                .status("RETURNED").date("2026-04-24")
                .build();

        when(messageConverter.fromMessage(rawMessage)).thenReturn(message);
        when(memberClient.resolveRecipientEmail(11L)).thenReturn("returned@iswear.com");
        when(lendingClient.resolveBookId(11L)).thenReturn(11L);
        when(bookClient.resolveBookTitle(11L)).thenReturn("Animal Farm");

        consumer.receiveNotification(rawMessage);

        verify(notificationService).createLoanReturnedNotification(
                eq("returned@iswear.com"), eq(11L), eq("Animal Farm"), eq("2026-04-24"), eq("RETURNED")
        );

        verify(statsService).markReturned(eq(11L));
    }

    @Test
    void receiveNotification_fineIssued_callsCreateFineIssuedNotification() throws JMSException {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(12L).memberId(22L).bookCopyId(32L)
                .type(NotificationType.FINE_ISSUED)
                .fineAmount(150.0).date("2026-04-24")
                .build();

        when(messageConverter.fromMessage(rawMessage)).thenReturn(message);
        when(memberClient.resolveRecipientEmail(22L)).thenReturn("joseph@fined.com");
        when(lendingClient.resolveBookId(12L)).thenReturn(32L);
        when(bookClient.resolveBookTitle(32L)).thenReturn("Animal Farm");

        consumer.receiveNotification(rawMessage);

        verify(notificationService).createFineIssuedNotification(
                eq("joseph@fined.com"), eq(12L), eq("Animal Farm"), eq(150.0), eq("2026-04-24")
        );
    }

    @Test
    void receiveNotification_reservationCreated_callsCreateReservationCreatedNotification() throws JMSException {
        ReservationEventMessage message = ReservationEventMessage.builder()
                .reservationId(50L).memberId(70L).bookCopyId(80L)
                .type(NotificationType.RESERVATION_CREATED)
                .date("2026-04-25")
                .whenAvailableDate(LocalDate.parse("2026-04-28"))
                .status("ACTIVE")
                .build();

        when(messageConverter.fromMessage(rawMessage)).thenReturn(message);
        when(memberClient.resolveRecipientEmail(70L)).thenReturn("reservation@books.com");
        when(bookClient.resolveBookTitle(80L)).thenReturn("Terrible Train Phantom");

        consumer.receiveNotification(rawMessage);

        verify(notificationService).createReservationCreatedNotification(
                eq("reservation@books.com"), eq("Terrible Train Phantom"),
                eq(50L), eq("2026-04-25"), eq("2026-04-28"), eq("ACTIVE")
        );
    }
}