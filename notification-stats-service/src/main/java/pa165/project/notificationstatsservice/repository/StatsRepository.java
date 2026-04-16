package pa165.project.notificationstatsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pa165.project.notificationstatsservice.model.Stats;
import java.util.List;

/* *
 * Interface definition for Statistics repository
 * */

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT s FROM Stats s WHERE s.returned = :returned")
    List<Stats> findByReturned(Boolean returned);

    @Query("SELECT s FROM Stats s WHERE s.bookTitle = :bookTitle")
    List<Stats> findByBookTitle(String bookTitle);
}
