package pa165.project.lendingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pa165.project.lendingservice.messaging.LendingMessageProducer;
import pa165.project.lendingservice.model.Reservation;
import pa165.project.lendingservice.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final LendingMessageProducer lendingMessageProducer;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getReservationsByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    @Override
    public List<Reservation> getReservationsByBookId(Long bookId) {
        return reservationRepository.findByBookId(bookId);
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);

        lendingMessageProducer.sendReservationCreated(
                savedReservation.getId(),
                savedReservation.getMemberId(),
                savedReservation.getBookId(),
                savedReservation.getWhenAvailableDate().toString()
        );

        return savedReservation;
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);

        lendingMessageProducer.sendReservationUpdated(
                savedReservation.getId(),
                savedReservation.getMemberId(),
                savedReservation.getBookId(),
                savedReservation.getStatus(),
                savedReservation.getWhenAvailableDate().toString()
        );

        return savedReservation;
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}