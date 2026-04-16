package pa165.project.notificationstatsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/* *
 * Model for Statistics
 * */

@Entity
@Table(name="stats")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private Boolean returned;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stats stats)) return false;
        return Objects.equals(getId(), stats.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
