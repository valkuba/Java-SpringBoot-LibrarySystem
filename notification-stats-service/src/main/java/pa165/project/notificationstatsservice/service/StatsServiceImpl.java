package pa165.project.notificationstatsservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pa165.project.notificationstatsservice.model.Stats;
import pa165.project.notificationstatsservice.repository.StatsRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* *
 * Implementation of the Stats Service Methods
 * Provides business logic for borrowing statistics and book popularity reports.
 * */

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Stats> getAllStats() {
        return statsRepository.findAll();
    }

    // Returns HashMap of a bookTitles and amount of borrowings for each one
    @Transactional(readOnly = true)
    @Override
    public Map<String, Long> getTopNBooks(int limit) {
        List<Stats> allRecords = statsRepository.findAll();

        // Group by book title and count the number of borrowings for each title
        Map<String, Long> borrowCountByBook = allRecords.stream()
                .collect(Collectors.groupingBy(
                        Stats::getBookTitle,
                        Collectors.counting()
                ));

        // Sort the map by borrow count in descending order and limit the results
        return borrowCountByBook.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}

