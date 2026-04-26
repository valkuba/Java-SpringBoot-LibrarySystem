package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pa165.project.lendingservice.model.Reservation;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTests {

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    @BeforeEach
    public void setup() {
        reservationRepository.deleteAll();

        reservation1 = reservationRepository.save(Reservation.builder()
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .whenAvailableDate(LocalDate.of(2026, 1, 10))
                .status("PENDING")
                .build());

        reservation2 = reservationRepository.save(Reservation.builder()
                .memberId(10L)
                .bookId(101L)
                .reservationDate(LocalDate.of(2026, 2, 1))
                .whenAvailableDate(LocalDate.of(2026, 2, 10))
                .status("FULFILLED")
                .build());

        reservation3 = reservationRepository.save(Reservation.builder()
                .memberId(20L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 3, 1))
                .whenAvailableDate(LocalDate.of(2026, 3, 10))
                .status("PENDING")
                .build());
    }

    @Test
    public void findAll_seededData_returnsAllThreeReservations() {
        var result = reservationRepository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    public void findById_existingId_returnsReservation() {
        var result = reservationRepository.findById(reservation1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(10L);
        assertThat(result.get().getStatus()).isEqualTo("PENDING");
    }

    @Test
    public void findById_nonExistingId_returnsEmpty() {
        var result = reservationRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void save_reservationWithNullId_assignsGeneratedId() {
        var newReservation = Reservation.builder()
                .memberId(30L)
                .bookId(200L)
                .reservationDate(LocalDate.of(2026, 4, 1))
                .status("PENDING")
                .build();

        var saved = reservationRepository.save(newReservation);

        assertThat(saved.getId()).isNotNull();
        assertThat(reservationRepository.findById(saved.getId())).isPresent();
    }

    @Test
    public void findByMemberId_memberWithMultipleReservations_returnsOnlyHisReservations() {
        var result = reservationRepository.findByMemberId(10L);

        assertThat(result).hasSize(2);
    }

    @Test
    public void findByMemberId_nonExistingMember_returnsEmptyList() {
        var result = reservationRepository.findByMemberId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void findByBookId_bookWithMultipleReservations_returnsAllMatching() {
        var result = reservationRepository.findByBookId(100L);

        assertThat(result).hasSize(2);
    }

    @Test
    public void findByBookId_nonExistingBook_returnsEmptyList() {
        var result = reservationRepository.findByBookId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void update_existingReservation_updatesStatus() {
        var updated = Reservation.builder()
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .status("FULFILLED")
                .build();

        var result = reservationRepository.save(updated);

        assertThat(result.getStatus()).isEqualTo("FULFILLED");
        var foundUpdated = reservationRepository.findById(updated.getId());
        assertThat(foundUpdated).isPresent();
        assertThat(foundUpdated.get().getStatus()).isEqualTo("FULFILLED");
    }

    @Test
    public void deleteById_existingId_removesReservation() {
        reservationRepository.deleteById(reservation2.getId());

        assertThat(reservationRepository.findById(reservation2.getId())).isEmpty();
        assertThat(reservationRepository.findAll()).hasSize(2);
    }

    @Test
    public void deleteById_nonExistingId_doesNotChangeTotalCount() {
        reservationRepository.deleteById(999L);

        assertThat(reservationRepository.findAll()).hasSize(3);
    }
}
