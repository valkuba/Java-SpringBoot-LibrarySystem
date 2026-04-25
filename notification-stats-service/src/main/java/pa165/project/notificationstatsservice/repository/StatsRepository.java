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
    List<Stats> findByReturned(Boolean returned);

    List<Stats> findByBookTitle(String bookTitle);

    @Query("SELECT s.bookTitle, COUNT(s) as borrowCount FROM Stats s GROUP BY s.bookTitle ORDER BY borrowCount DESC")
    List<Object[]> findBookBorrowCounts();
}
