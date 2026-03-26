package pa165.project.lendingservice.repository;

import pa165.project.lendingservice.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByBookId(Long bookId);
    Reservation update(Reservation reservation);
    void deleteById(Long id);
}