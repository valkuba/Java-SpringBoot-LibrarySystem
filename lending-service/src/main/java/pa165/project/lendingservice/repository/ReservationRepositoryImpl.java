package pa165.project.lendingservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.lendingservice.model.Reservation;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(idCounter.getAndIncrement());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public List<Reservation> findByMemberId(Long memberId) {
        return reservations.values().stream()
                .filter(r -> Objects.equals(r.getMemberId(), memberId))
                .toList();
    }

    @Override
    public List<Reservation> findByBookId(Long bookId) {
        return reservations.values().stream()
                .filter(r -> Objects.equals(r.getBookId(), bookId))
                .toList();
    }

    @Override
    public Reservation update(Reservation reservation) {
        if (reservation.getId() == null || !reservations.containsKey(reservation.getId())) {
            throw new NoSuchElementException("Reservation not found with id: " + reservation.getId());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }
}