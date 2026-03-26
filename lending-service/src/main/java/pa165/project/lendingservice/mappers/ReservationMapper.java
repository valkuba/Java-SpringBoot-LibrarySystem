package pa165.project.lendingservice.mappers;

import org.mapstruct.Mapper;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationDto toDto(Reservation reservation);
    Reservation toModel(ReservationDto reservationDto);
}
