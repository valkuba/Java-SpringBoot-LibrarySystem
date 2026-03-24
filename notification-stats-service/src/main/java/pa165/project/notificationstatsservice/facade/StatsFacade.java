package pa165.project.notificationstatsservice.facade;

import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import java.util.List;

/* *
 * Interface for the StatsFacade
 * */
public interface StatsFacade {
    List<StatsDto> getAllStats();
    public List<PopularBookDto> getTopNBooks(int limit);
}
