package pa165.project.notificationstatsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/* *
 * Model for Statistics
 *
 * */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {
    private Long id;
    private String bookTitle;
    private Boolean returned;
}
