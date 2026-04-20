package pa165.project.notificationstatsservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pa165.project.notificationstatsservice.dto.LendingNotificationDto;
import pa165.project.notificationstatsservice.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LendingNotificationConsumerImpl implements LendingNotificationConsumer{
    private final NotificationService notificationService;

    @JmsListener(destination = "${app.messaging.overdue-queue}")
    @Override
    public void receiveNotification(LendingNotificationDto dto) {
        log.info("Received JMS message type={} for email={}",
                dto.getType(), dto.getRecipientEmail());

        switch (dto.getType()) {
            case OVERDUE -> notificationService.createOverdueNotification(
                    dto.getRecipientEmail(),
                    dto.getBookTitle()
            );
            case FINE_ISSUED -> notificationService.createOverdueNotification(
                    dto.getRecipientEmail(),
                    dto.getBookTitle() + " (fine: " + dto.getFineAmount() + " CZK)"
            );
            case RESERVATION_READY -> notificationService.createOverdueNotification(
                    dto.getRecipientEmail(),
                    dto.getBookTitle() + " (available until: " + dto.getWhenAvailableDate() + ")"
            );
            default -> log.warn("Unknown message type: {}", dto.getType());
        }
    }
}
