package pa165.project.notificationstatsservice.mapper;

import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.model.Notification;
import org.mapstruct.Mapper;

/* *
 * Mapper class for converting between Notification and NotificationDto.
 * Uses Mapper annotation for Bean creation.
 * */
@Mapper(componentModel = "spring")
public class NotificationMapper {
    public NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .recipientEmail(notification.getRecipientEmail())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public Notification toModel(CreateNotificationDto dto) {
        return Notification.builder()
                .message(dto.getMessage())
                .recipientEmail(dto.getRecipientEmail())
                .build();
    }
}
