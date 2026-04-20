package pa165.project.notificationstatsservice.consumer;

import pa165.project.notificationstatsservice.dto.LendingNotificationDto;

public interface LendingNotificationConsumer {
    void receiveNotification(LendingNotificationDto dto);
}
