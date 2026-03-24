package pa165.project.notificationstatsservice.repository;

import pa165.project.notificationstatsservice.model.Stats;
import java.util.List;
import java.util.Optional;

/* *
 * Interface definition for Statistics repository
 * */

public interface StatsRepository {
    List<Stats> findAll();
    Optional<Stats> findById(Long id);
    List<Stats> findByReturned(boolean returned);
}
