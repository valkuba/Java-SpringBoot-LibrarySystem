package pa165.project.lendingservice.facade;

import pa165.project.lendingservice.dto.ReservationDto;
import java.util.List;
import java.util.Optional;

public interface ReservationFacade {
    List<ReservationDto> getAllReservations();
    Optional<ReservationDto> getReservationById(Long id);
    List<ReservationDto> getReservationsByMemberId(Long memberId);
    List<ReservationDto> getReservationsByBookId(Long bookId);
    ReservationDto createReservation(ReservationDto reservationDto);
    ReservationDto updateReservation(ReservationDto reservationDto);
    void deleteReservation(Long id);
}