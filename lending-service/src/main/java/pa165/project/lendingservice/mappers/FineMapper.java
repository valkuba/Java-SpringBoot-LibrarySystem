package pa165.project.lendingservice.mappers;

import org.mapstruct.Mapper;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.model.Fine;

@Mapper(componentModel = "spring")
public interface FineMapper {
    FineDto toDto(Fine fine);
    Fine toModel(FineDto fineDto);
}
