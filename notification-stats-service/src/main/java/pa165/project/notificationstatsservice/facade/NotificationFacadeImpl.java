package pa165.project.notificationstatsservice.facade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.mapper.NotificationMapper;
import pa165.project.notificationstatsservice.model.Notification;
import pa165.project.notificationstatsservice.service.NotificationService;
import java.util.List;
import java.util.Optional;

/* *
 * Implementation of the Notification Facade methods defined by interface
 * */

@Component
@RequiredArgsConstructor
public class NotificationFacadeImpl implements NotificationFacade{
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> getAllNotifications() {
        return notificationService.getAllNotifications().stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public Optional<NotificationDto> getById(Long id) {
        return notificationService.getById(id)
                .map(notificationMapper::toDto);
    }

    // Dto -> model, calls service, returns Dto
    @Override
    public NotificationDto createNotification(@Valid CreateNotificationDto dto) {
        Notification n = notificationMapper.toModel(dto);
        Notification saved = notificationService.createNotification(n);
        return notificationMapper.toDto(saved);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationService.deleteNotification(id);
    }

    // calls service and creates the dto by itself
    @Override
    public NotificationDto createOverdueNotification(String recipientEmail, String bookTitle) {
        Notification n = notificationService.createOverdueNotification(recipientEmail, bookTitle);
        return notificationMapper.toDto(n);
    }

    @Override
    public List<NotificationDto> getAllSortedByDate() {
        return notificationService.getAllSortedByDate().stream()
                .map(notificationMapper::toDto)
                .toList();
    }
}
