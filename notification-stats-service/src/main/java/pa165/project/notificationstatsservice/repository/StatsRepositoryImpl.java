package pa165.project.notificationstatsservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.notificationstatsservice.model.Stats;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/* *
 * Implementation of the statistics repository
 * */

@Repository
public class StatsRepositoryImpl implements StatsRepository {

    private final AtomicLong idGen = new AtomicLong(1);
    // Mock data, just for the purpose of Milestone1
    private final List<Stats> mockBorrowings = List.of(
            Stats.builder().id(idGen.getAndIncrement()).bookTitle("Clean Code").returned(true).build(),
            Stats.builder().id(idGen.getAndIncrement()).bookTitle("Clean Code").returned(false).build(),
            Stats.builder().id(idGen.getAndIncrement()).bookTitle("Spring Boot").returned(true).build()
    );

    @Override
    public List<Stats> findAll() {
        return mockBorrowings;
    }

    @Override
    public Optional<Stats> findById(Long id) {
        return mockBorrowings.stream().filter(s -> s.getId().equals(id)).findFirst();
    }


    @Override
    public List<Stats> findByReturned(boolean returned) {
        return mockBorrowings.stream().filter(s -> s.getReturned().equals(returned)).toList();    }
}
