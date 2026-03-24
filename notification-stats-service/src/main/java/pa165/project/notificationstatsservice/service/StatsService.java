package pa165.project.notificationstatsservice.service;

import pa165.project.notificationstatsservice.model.Stats;
import java.util.List;
import java.util.Map;

/* *
 * Interface defining the Stats service methods
 * */

public interface StatsService {
    List<Stats> getAllStats();
    Map<String, Long> getTopNBooks(int limit);
}
