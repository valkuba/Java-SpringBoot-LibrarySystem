package pa165.project.notificationstatsservice.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.client.BookClient;
import pa165.project.notificationstatsservice.client.MemberClient;
import pa165.project.notificationstatsservice.service.NotificationService;

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
    void receiveNotification_loanCreated_callsCreateLoanCreatedNotification() {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(10L)
                .memberId(20L)
                .bookCopyId(30L)
                .type(NotificationType.LOAN_CREATED)
                .date("2026-04-24")
                .build();

        when(memberClient.resolveRecipientEmail(20L)).thenReturn("user@test.com");
        when(bookClient.resolveBookTitle(30L)).thenReturn("Blood of elves");

        consumer.receiveNotification(message);

        verify(notificationService).createLoanCreatedNotification(
                eq("user@test.com"), eq("Blood of elves"), eq(10L), eq("2026-04-24")
        );
        verify(notificationService, never()).createLoanReturnedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createFineIssuedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyDouble(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void receiveNotification_loanReturned_callsCreateLoanReturnedNotification() {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(11L)
                .memberId(21L)
                .bookCopyId(31L)
                .type(NotificationType.LOAN_RETURNED)
                .status("RETURNED")
                .date("2026-04-25")
                .build();

        when(memberClient.resolveRecipientEmail(21L)).thenReturn("returned@test.com");
        when(bookClient.resolveBookTitle(31L)).thenReturn("Last wish");

        consumer.receiveNotification(message);

        verify(notificationService).createLoanReturnedNotification(
                eq("returned@test.com"), eq("Last wish"), eq(11L), eq("2026-04-25"), eq("RETURNED")
        );
    }

    @Test
    void receiveNotification_fineIssued_callsCreateFineIssuedNotification() {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(12L)
                .memberId(22L)
                .bookCopyId(32L)
                .type(NotificationType.FINE_ISSUED)
                .fineAmount(150.0)
                .date("2026-04-26")
                .build();

        when(memberClient.resolveRecipientEmail(22L)).thenReturn("fine@test.com");
        when(bookClient.resolveBookTitle(32L)).thenReturn("Sword of destiny");

        consumer.receiveNotification(message);

        verify(notificationService).createFineIssuedNotification(
                eq("fine@test.com"), eq("Sword of destiny"), eq(12L), eq(150.0), eq("2026-04-26")
        );
    }

    @Test
    void receiveNotification_invalidMessage_doesNothing() {
        consumer.receiveNotification(null);

        verify(notificationService, never()).createLoanCreatedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createLoanReturnedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createFineIssuedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyDouble(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void receiveNotification_messageWithoutType_doesNothing() {
        LoanEventMessage message = LoanEventMessage.builder()
                .loanId(13L)
                .memberId(23L)
                .bookCopyId(33L)
                .build();

        consumer.receiveNotification(message);

        verify(notificationService, never()).createLoanCreatedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createLoanReturnedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(notificationService, never()).createFineIssuedNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyDouble(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }
}

