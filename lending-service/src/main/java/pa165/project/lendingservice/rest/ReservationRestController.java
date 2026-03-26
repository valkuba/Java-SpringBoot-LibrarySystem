package pa165.project.lendingservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.facade.ReservationFacade;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation Management API")
public class ReservationRestController {

    private final ReservationFacade reservationFacade;

    @Operation(summary = "Get all reservations")
    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return reservationFacade.getAllReservations();
    }

    @Operation(summary = "Get a reservation by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        return reservationFacade.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get reservations by member ID")
    @GetMapping("/member/{memberId}")
    public List<ReservationDto> getReservationsByMember(@PathVariable Long memberId) {
        return reservationFacade.getReservationsByMemberId(memberId);
    }

    @Operation(summary = "Get reservations by book ID")
    @GetMapping("/book/{bookId}")
    public List<ReservationDto> getReservationsByBook(@PathVariable Long bookId) {
        return reservationFacade.getReservationsByBookId(bookId);
    }

    @Operation(summary = "Create a new reservation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@RequestBody ReservationDto reservationDto) {
        return reservationFacade.createReservation(reservationDto);
    }

    @Operation(summary = "Update an existing reservation")
    @PutMapping("/{id}")
    public ReservationDto updateReservation(@PathVariable Long id, @RequestBody ReservationDto reservationDto) {
        reservationDto.setId(id);
        return reservationFacade.updateReservation(reservationDto);
    }

    @Operation(summary = "Delete a reservation")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id) {
        reservationFacade.deleteReservation(id);
    }
}