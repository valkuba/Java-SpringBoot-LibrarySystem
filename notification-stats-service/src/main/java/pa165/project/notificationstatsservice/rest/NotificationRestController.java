package pa165.project.notificationstatsservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.facade.NotificationFacade;


import java.util.List;

/* *
 * REST controller for API endpoints for notifications
 * */

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "API for managing notifications")
public class NotificationRestController {
    private final NotificationFacade notificationFacade;

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        return ResponseEntity.ok(notificationFacade.getAllNotifications());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id) {
        return notificationFacade.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new notification")
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody CreateNotificationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationFacade.createNotification(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationFacade.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/overdue")
    @Operation(summary = "Create overdue notification")
    public ResponseEntity<NotificationDto> createOverdueNotification(@RequestParam String recipientEmail, @RequestParam String bookTitle) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationFacade.createOverdueNotification(recipientEmail, bookTitle));
    }

    @GetMapping("/sorted")
    @Operation(summary = "Get notifications sorted by date")
    public ResponseEntity<List<NotificationDto>> getAllSortedByDate() {
        return ResponseEntity.ok(notificationFacade.getAllSortedByDate());
    }

}
