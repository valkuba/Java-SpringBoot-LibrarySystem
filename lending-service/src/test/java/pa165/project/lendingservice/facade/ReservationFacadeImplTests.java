package pa165.project.lendingservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.mappers.ReservationMapper;
import pa165.project.lendingservice.model.Reservation;
import pa165.project.lendingservice.service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReservationFacadeImplTests {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationFacadeImpl reservationFacade;

    @Test
    public void getAllReservations_reservationsExist_returnsMappedDtos() {
        var reservation1 = Reservation.builder().id(1L).build();
        var reservation2 = Reservation.builder().id(2L).build();
        var reservation3 = Reservation.builder().id(3L).build();
        var dto1 = ReservationDto.builder().id(1L).build();
        var dto2 = ReservationDto.builder().id(2L).build();
        var dto3 = ReservationDto.builder().id(3L).build();
        Mockito.when(reservationService.getAllReservations()).thenReturn(List.of(reservation1, reservation2, reservation3));
        Mockito.when(reservationMapper.toDto(reservation1)).thenReturn(dto1);
        Mockito.when(reservationMapper.toDto(reservation2)).thenReturn(dto2);
        Mockito.when(reservationMapper.toDto(reservation3)).thenReturn(dto3);

        var result = reservationFacade.getAllReservations();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(ReservationDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getReservationById_existingId_returnsMappedDto() {
        Mockito.when(reservationMapper.toDto(Mockito.any())).thenReturn(ReservationDto.builder().id(1L).build());
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.of(Reservation.builder().id(1L).build()));

        var result = reservationFacade.getReservationById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    public void getReservationById_nonExistingId_returnsEmpty() {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.empty());

        var result =  reservationFacade.getReservationById(1L);
        assertThat(result).isEmpty();
    }

    @Test
    public void getReservationsByMemberId_memberWithReservations_returnsMappedDtos() {
        var reservation1 = Reservation.builder().id(1L).memberId(1L).build();
        var reservation2 = Reservation.builder().id(2L).memberId(1L).build();
        var dto1 = ReservationDto.builder().id(1L).memberId(1L).build();
        var dto2 = ReservationDto.builder().id(2L).memberId(1L).build();

        Mockito.when(reservationMapper.toDto(reservation1)).thenReturn(dto1);
        Mockito.when(reservationMapper.toDto(reservation2)).thenReturn(dto2);

        Mockito.when(reservationService.getReservationsByMemberId(1L)).thenReturn(List.of(reservation1, reservation2));

        var result = reservationFacade.getReservationsByMemberId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ReservationDto::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    public void getReservationsByBookId_bookWithReservations_returnsMappedDtos() {
        var reservation1 = Reservation.builder().id(1L).bookId(1L).build();
        var reservation2 = Reservation.builder().id(2L).bookId(1L).build();
        var dto1 = ReservationDto.builder().id(1L).bookId(1L).build();
        var dto2 = ReservationDto.builder().id(2L).bookId(1L).build();

        Mockito.when(reservationMapper.toDto(reservation1)).thenReturn(dto1);
        Mockito.when(reservationMapper.toDto(reservation2)).thenReturn(dto2);

        Mockito.when(reservationService.getReservationsByBookId(1L)).thenReturn(List.of(reservation1, reservation2));

        var result = reservationFacade.getReservationsByBookId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ReservationDto::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    public void createReservation_validDto_returnsMappedDto() {
        Mockito.when(reservationMapper.toDto(Mockito.any())).thenReturn(ReservationDto.builder().id(1L).build());
        Mockito.when(reservationService.createReservation(Mockito.any())).thenReturn(Reservation.builder().id(1L).build());

        var result = reservationFacade.createReservation(ReservationDto.builder().id(1L).build());

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void updateReservation_existingReservation_returnsMappedDto() {
        var date = LocalDate.now();
        Mockito.when(reservationMapper.toDto(Mockito.any())).thenReturn(ReservationDto.builder().id(1L).reservationDate(date).build());
        Mockito.when(reservationService.updateReservation(Mockito.any())).thenReturn(Reservation.builder().id(1L).reservationDate(date).build());

        var result = reservationFacade.updateReservation(ReservationDto.builder().reservationDate(date).id(1L).build());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getReservationDate()).isEqualTo(date);
    }

    @Test
    public void deleteReservation_existingId_callsServiceDeleteReservation() {
        reservationFacade.deleteReservation(1L);

        Mockito.verify(reservationService).deleteReservation(1L);
    }
}
