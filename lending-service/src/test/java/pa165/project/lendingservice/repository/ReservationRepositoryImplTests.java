package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pa165.project.lendingservice.model.Reservation;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ReservationRepositoryImplTests {

    @Autowired
    private ReservationRepositoryImpl reservationRepository;

    @BeforeEach
    public void setup() {
        reservationRepository.findAll().forEach(r -> reservationRepository.deleteById(r.getId()));

        reservationRepository.save(Reservation.builder()
                .id(1L)
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .whenAvailableDate(LocalDate.of(2026, 1, 10))
                .status("PENDING")
                .build());

        reservationRepository.save(Reservation.builder()
                .id(2L)
                .memberId(10L)
                .bookId(101L)
                .reservationDate(LocalDate.of(2026, 2, 1))
                .whenAvailableDate(LocalDate.of(2026, 2, 10))
                .status("FULFILLED")
                .build());

        reservationRepository.save(Reservation.builder()
                .id(3L)
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
        assertThat(result).extracting(Reservation::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void findById_existingId_returnsReservation() {
        var result = reservationRepository.findById(1L);

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
    public void save_reservationWithExistingId_overwritesReservation() {
        var updated = Reservation.builder()
                .id(1L)
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .status("FULFILLED")
                .build();

        reservationRepository.save(updated);

        var foundUpdated = reservationRepository.findById(1L);
        assertThat(foundUpdated).isPresent();
        assertThat(foundUpdated.get().getStatus()).isEqualTo("FULFILLED");
        assertThat(reservationRepository.findAll()).hasSize(3);
    }

    @Test
    public void findByMemberId_memberWithMultipleReservations_returnsOnlyHisReservations() {
        var result = reservationRepository.findByMemberId(10L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Reservation::getId).containsExactlyInAnyOrder(1L, 2L);
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
        assertThat(result).extracting(Reservation::getId).containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    public void findByBookId_nonExistingBook_returnsEmptyList() {
        var result = reservationRepository.findByBookId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void update_existingReservation_updatesStatus() {
        var updated = Reservation.builder()
                .id(1L)
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .status("FULFILLED")
                .build();

        var result = reservationRepository.update(updated);

        assertThat(result.getStatus()).isEqualTo("FULFILLED");
        var foundUpdated = reservationRepository.findById(1L);
        assertThat(foundUpdated).isPresent();
        assertThat(foundUpdated.get().getStatus()).isEqualTo("FULFILLED");
    }

    @Test
    public void update_nonExistingReservation_throwsNoSuchElementException() {
        var nonExisting = Reservation.builder()
                .id(999L)
                .memberId(10L)
                .bookId(100L)
                .status("PENDING")
                .build();

        assertThatThrownBy(() -> reservationRepository.update(nonExisting))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("999");
    }

    @Test
    public void deleteById_existingId_removesReservation() {
        reservationRepository.deleteById(2L);

        assertThat(reservationRepository.findById(2L)).isEmpty();
        assertThat(reservationRepository.findAll()).hasSize(2);
    }

    @Test
    public void deleteById_nonExistingId_doesNotChangeTotalCount() {
        reservationRepository.deleteById(999L);

        assertThat(reservationRepository.findAll()).hasSize(3);
    }
}
