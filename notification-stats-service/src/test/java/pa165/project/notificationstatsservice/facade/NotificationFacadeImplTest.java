package pa165.project.notificationstatsservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.mapper.NotificationMapper;
import pa165.project.notificationstatsservice.model.Notification;
import pa165.project.notificationstatsservice.service.NotificationService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationFacadeImplTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationFacadeImpl notificationFacadeImpl;

    @Test
    void getAllNotifications_shouldMapAndReturnAll() {
        Notification model = Notification.builder().id(1L).message("message").build();
        NotificationDto dto = NotificationDto.builder().id(1L).message("message").build();

        when(notificationService.getAllNotifications()).thenReturn(List.of(model));
        when(notificationMapper.toDto(model)).thenReturn(dto);

        List<NotificationDto> result = notificationFacadeImpl.getAllNotifications();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getById_whenNotFound_shouldReturnEmpty() {
        when(notificationService.getById(99L)).thenReturn(Optional.empty());

        Optional<NotificationDto> result = notificationFacadeImpl.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void createNotification_shouldDelegateAndMap() {
        Notification model = Notification.builder().id(1L).message("Hello").build();
        NotificationDto outputDto = NotificationDto.builder().id(1L).message("Hello").build();

        when(notificationMapper.toModel(any())).thenReturn(model);
        when(notificationService.createNotification(model)).thenReturn(model);
        when(notificationMapper.toDto(model)).thenReturn(outputDto);

        CreateNotificationDto createDto = CreateNotificationDto.builder()
                .message("Hello").recipientEmail("a@b.com").build();
        NotificationDto result = notificationFacadeImpl.createNotification(createDto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(notificationService).createNotification(model);
    }

    @Test
    void deleteNotification_shouldDelegateToService() {
        notificationFacadeImpl.deleteNotification(1L);

        verify(notificationService).deleteNotification(1L);
    }

    @Test
    void createOverdueNotification_shouldDelegateToServiceAndMap() {
        Notification model = Notification.builder().id(1L)
                .message("Your borrowed book 'Blood of elves' is overdue.")
                .recipientEmail("test@gmail.com")
                .build();
        NotificationDto dto = NotificationDto.builder().id(1L).build();

        when(notificationService.createOverdueNotification("test@gmail.com", "Blood of elves")).thenReturn(model);
        when(notificationMapper.toDto(model)).thenReturn(dto);

        NotificationDto result = notificationFacadeImpl.createOverdueNotification("test@gmail.com", "Blood of elves");

        assertThat(result).isEqualTo(dto);
        verify(notificationService).createOverdueNotification("test@gmail.com", "Blood of elves");
    }

    @Test
    void getAllSortedByDate_shouldMapAndReturnAll() {
        Notification model = Notification.builder().id(1L).build();
        NotificationDto dto = NotificationDto.builder().id(1L).build();

        when(notificationService.getAllSortedByDate()).thenReturn(List.of(model));
        when(notificationMapper.toDto(model)).thenReturn(dto);

        List<NotificationDto> result = notificationFacadeImpl.getAllSortedByDate();

        assertThat(result).hasSize(1);
        verify(notificationService).getAllSortedByDate();
    }
}