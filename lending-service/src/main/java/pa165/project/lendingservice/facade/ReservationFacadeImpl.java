package pa165.project.lendingservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.mappers.ReservationMapper;
import pa165.project.lendingservice.model.Reservation;
import pa165.project.lendingservice.service.ReservationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationFacadeImpl implements ReservationFacade {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @Override
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservationDto> getReservationById(Long id) {
        return reservationService.getReservationById(id)
                .map(reservationMapper::toDto);
    }

    @Override
    public List<ReservationDto> getReservationsByMemberId(Long memberId) {
        return reservationService.getReservationsByMemberId(memberId).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByBookId(Long bookId) {
        return reservationService.getReservationsByBookId(bookId).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.toModel(reservationDto);
        return reservationMapper.toDto(reservationService.createReservation(reservation));
    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.toModel(reservationDto);
        return reservationMapper.toDto(reservationService.updateReservation(reservation));
    }

    @Override
    public void deleteReservation(Long id) {
        reservationService.deleteReservation(id);
    }
}