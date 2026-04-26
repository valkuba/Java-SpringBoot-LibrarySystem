package pa165.project.lendingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.messaging.LendingMessageProducer;
import pa165.project.lendingservice.model.Reservation;
import pa165.project.lendingservice.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTests {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private LendingMessageProducer lendingMessageProducer;

    @Test
    public void getAllReservations_reservationsExist_returnsAllReservations() {
        Mockito.when(reservationRepository.findAll()).thenReturn(List.of(
                Reservation.builder().id(1L).build(),
                Reservation.builder().id(2L).build(),
                Reservation.builder().id(3L).build()
        ));

        var reservations = reservationService.getAllReservations();

        assertThat(reservations).hasSize(3);
        assertThat(reservations).extracting(Reservation::getId).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void getReservationById_existingId_returnsReservation() {
        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.of(Reservation.builder().id(1L).build()));

        var reservation = reservationService.getReservationById(1L);

        assertThat(reservation).isPresent();
        assertThat(reservation.get().getId()).isEqualTo(1L);
    }

    @Test
    public void getReservationById_nonExistingId_returnsEmpty() {
        Mockito.when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        var reservation = reservationService.getReservationById(1L);

        assertThat(reservation).isEmpty();
    }

    @Test
    public void getReservationsByMemberId_memberWithReservations_returnsHisReservations() {
        Mockito.when(reservationRepository.findByMemberId(1L)).thenReturn(List.of(
                Reservation.builder().memberId(1L).id(1L).build(),
                Reservation.builder().memberId(1L).id(3L).build()
        ));

        var  reservations = reservationService.getReservationsByMemberId(1L);

        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getId).containsExactly(1L, 3L);
    }

    @Test
    public void getReservationsByMemberId_nonExistingMember_returnsEmptyList() {
        Mockito.when(reservationRepository.findByMemberId(1L)).thenReturn(List.of());

        var reservations = reservationService.getReservationsByMemberId(1L);

        assertThat(reservations).isEmpty();
    }

    @Test
    public void getReservationsByBookId_bookWithReservations_returnsMatchingReservations() {
        Mockito.when(reservationRepository.findByBookId(1L)).thenReturn(List.of(
                Reservation.builder().bookId(1L).id(1L).build(),
                Reservation.builder().bookId(1L).id(3L).build()
        ));

        var  reservations = reservationService.getReservationsByBookId(1L);

        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getId).containsExactly(1L, 3L);
    }

    @Test
    public void getReservationsByBookId_nonExistingBook_returnsEmptyList() {
        Mockito.when(reservationRepository.findByBookId(1L)).thenReturn(List.of());

        var reservations = reservationService.getReservationsByBookId(1L);

        assertThat(reservations).isEmpty();
    }

    @Test
    public void createReservation_validReservation_savesAndSendsMessage() {
        var input = Reservation.builder()
                .memberId(10L)
                .bookId(20L)
                .whenAvailableDate(java.time.LocalDate.of(2026, 1, 10))
                .build();

        var saved = Reservation.builder()
                .id(1L)
                .memberId(10L)
                .bookId(20L)
                .whenAvailableDate(java.time.LocalDate.of(2026, 1, 10))
                .build();

        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class)))
                .thenReturn(saved);

        var result = reservationService.createReservation(input);

        assertThat(result.getId()).isEqualTo(1L);

        Mockito.verify(lendingMessageProducer, Mockito.times(1))
                .sendReservationCreated(
                        Mockito.eq(1L),
                        Mockito.eq(10L),
                        Mockito.eq(20L),
                        Mockito.eq("2026-01-10")
                );
    }

    @Test
    public void updateReservation_validReservation_savesAndSendsMessage() {
        var input = Reservation.builder()
                .id(1L)
                .memberId(10L)
                .bookId(20L)
                .status("UPDATED")
                .whenAvailableDate(java.time.LocalDate.of(2026, 1, 10))
                .build();

        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class)))
                .thenReturn(input);

        var result = reservationService.updateReservation(input);

        assertThat(result.getStatus()).isEqualTo("UPDATED");

        Mockito.verify(lendingMessageProducer, Mockito.times(1))
                .sendReservationUpdated(
                        Mockito.eq(1L),
                        Mockito.eq(10L),
                        Mockito.eq(20L),
                        Mockito.eq("UPDATED"),
                        Mockito.eq("2026-01-10")
                );
    }

    @Test
    public void deleteReservation_existingId_callsRepositoryButNoMessage() {
        reservationService.deleteReservation(1L);

        Mockito.verify(reservationRepository, Mockito.times(1))
                .deleteById(1L);

        Mockito.verifyNoInteractions(lendingMessageProducer);
    }
}
