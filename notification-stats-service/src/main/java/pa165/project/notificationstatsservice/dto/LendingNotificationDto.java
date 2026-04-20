@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingNotificationDto {
    private String type;                // "OVERDUE", "FINE_ISSUED", "RESERVATION_READY"
    private String recipientEmail;
    private String bookTitle;
    private LocalDate dueDate;          // OVERDUE
    private Double fineAmount;          // FINE_ISSUED
    private LocalDate whenAvailableDate; // RESERVATION_READY
}