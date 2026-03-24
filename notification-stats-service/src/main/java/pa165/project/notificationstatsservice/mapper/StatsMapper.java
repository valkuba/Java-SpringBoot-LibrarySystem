package pa165.project.notificationstatsservice.mapper;

import org.mapstruct.Mapper;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.model.Stats;

/* *
 * Mapper class to convert Stats model to Dto
 * Uses Mapper annotation for Bean creation
 * */

@Mapper(componentModel = "spring")
public class StatsMapper {
    public StatsDto toDto(Stats stats) {
        return StatsDto.builder()
                .id(stats.getId())
                .bookTitle(stats.getBookTitle())
                .returned(stats.getReturned())
                .build();
    }
}
