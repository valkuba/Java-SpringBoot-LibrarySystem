package pa165.project.notificationstatsservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.mapper.StatsMapper;
import pa165.project.notificationstatsservice.service.StatsService;
import java.util.List;

/* *
 * Implementation of the StatsFacade interface, responsible for handling business logic related to statistics.
 * */

@Component
@RequiredArgsConstructor
public class StatsFacadeImpl implements StatsFacade{

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @Override
    public List<StatsDto> getAllStats() {
        return statsService.getAllStats().stream()
                .map(statsMapper::toDto)
                .toList();
    }

    // Maps the most popular books with their borrow count from service into the DTO for API
    @Override
    public List<PopularBookDto> getTopNBooks(int limit) {
        return statsService.getTopNBooks(limit).entrySet().stream()
                .map(e -> PopularBookDto.builder()
                        .bookTitle(e.getKey())
                        .borrowCount(e.getValue())
                        .build())
                .toList();
    }
}
