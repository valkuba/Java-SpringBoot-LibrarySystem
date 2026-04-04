package pa165.project.lendingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pa165.project.lendingservice.model.Reservation;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByBookId(Long bookId);
}