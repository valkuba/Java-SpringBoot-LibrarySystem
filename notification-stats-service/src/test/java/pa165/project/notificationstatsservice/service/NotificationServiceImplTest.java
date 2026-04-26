package pa165.project.notificationstatsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.model.Notification;
import pa165.project.notificationstatsservice.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Test
    void getAllNotifications_shouldReturnAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());

        when(notificationRepository.findAll()).thenReturn(notifications);

        assertEquals(notifications, notificationServiceImpl.getAllNotifications());
    }

    @Test
    void getById_shouldReturnNotification() {
        Notification notification = new Notification();
        when(notificationRepository.findById(any())).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationServiceImpl.getById(notification.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(notification);
    }

    @Test
    void getById_whenNotFound_shouldReturnEmpty() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationServiceImpl.getById(99L);
        
        assertThat(result).isEmpty();
    }

    @Test
    void createNotification_shouldSetCreatedAt() {
        Notification input = Notification.builder()
                .message("Hello").recipientEmail("test@gmail.com").build();

        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.createNotification(input);

        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(input);
    }

    @Test
    void createOverdueNotification_shouldContainBookTitle() {
        when(notificationRepository.save(any())).thenAnswer(i -> {
                Notification n = i.getArgument(0);
                n.setId(1L);
                return n;
            });

        Notification result = notificationServiceImpl
                .createOverdueNotification("test@gmail.com", "Blood of the elves");

        assertThat(result.getMessage()).contains("Blood of the elves");
        assertThat(result.getRecipientEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void createOverdueNotification_shouldContainOverdueWord() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl
                .createOverdueNotification("test@gmail.com", "Blood of the elves");

        assertThat(result.getMessage()).containsIgnoringCase("overdue");
    }

    @Test
    void deleteNotification_shouldCallRepository() {
        notificationServiceImpl.deleteNotification(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    void getAllSortedByDate_shouldReturnNewestFirst() {
        Notification older = Notification.builder().id(1L)
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0)).build();
        Notification newer = Notification.builder().id(2L)
                .createdAt(LocalDateTime.of(2025, 6, 1, 0, 0)).build();

        when(notificationRepository.findAll()).thenReturn(List.of(older, newer));

        List<Notification> result = notificationServiceImpl.getAllSortedByDate();

        assertThat(result.get(0).getId()).isEqualTo(2L); // new should be returned first
        assertThat(result.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void getAllSortedByDate_withSingleItem_shouldReturnIt() {
        Notification n = Notification.builder().id(1L)
                .createdAt(LocalDateTime.now()).build();

        when(notificationRepository.findAll()).thenReturn(List.of(n));

        List<Notification> result = notificationServiceImpl.getAllSortedByDate();

        assertThat(result).hasSize(1);
    }

    @Test
    void createLoanCreatedNotification_shouldCreateExpectedMessage() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.createLoanCreatedNotification(
                "test@gmail.com", "Blood of the elves", 10L, "2026-04-24");

        assertThat(result.getMessage()).isEqualTo("Loan 10 for 'Blood of the elves' was created on 2026-04-24.");
        assertThat(result.getRecipientEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createLoanReturnedNotification_shouldCreateExpectedMessage() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.createLoanReturnedNotification(
                "test@gmail.com",  11L, "2026-04-25", "RETURNED");

        assertThat(result.getMessage()).isEqualTo("Loan 11 was returned on 2026-04-25. Status: RETURNED.");
        assertThat(result.getRecipientEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createFineIssuedNotification_shouldCreateExpectedMessage() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.createFineIssuedNotification(
                "test@gmail.com", 12L, 150.0, "2026-04-26");

        assertThat(result.getMessage()).isEqualTo("Fine of 150.0 CZK was issued for loan 12 on 2026-04-26.");
        assertThat(result.getRecipientEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createReservationCreatedNotification_shouldCreateExpectedMessage() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.createReservationCreatedNotification(
                "test@gmail.com", "Blood of the elves", 13L, "2026-04-27", "2026-04-30", "ACTIVE");

        assertThat(result.getMessage()).isEqualTo(
                "Reservation 13 for 'Blood of the elves' was created on 2026-04-27. Available on: 2026-04-30. Status: ACTIVE.");
        assertThat(result.getRecipientEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void updateReservationCreatedNotification_shouldCreateExpectedMessage() {
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Notification result = notificationServiceImpl.updateReservationCreatedNotification(
                "test@test.com", "Please let me go", 13L, "2026-04-27", "2026-04-30", "ACTIVE");

        assertThat(result.getMessage()).isEqualTo(
                "Reservation 13 for 'Please let me go' was updated on 2026-04-27. Available on: 2026-04-30. Status: ACTIVE.");
        assertThat(result.getRecipientEmail()).isEqualTo("test@test.com");
        assertThat(result.getCreatedAt()).isNotNull();
        verify(notificationRepository).save(any(Notification.class));
    }
}