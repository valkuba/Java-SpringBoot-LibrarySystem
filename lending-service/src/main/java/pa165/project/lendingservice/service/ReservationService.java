package pa165.project.lendingservice.service;

import pa165.project.lendingservice.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    List<Reservation> getReservationsByMemberId(Long memberId);
    List<Reservation> getReservationsByBookId(Long bookId);
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Long id);
}