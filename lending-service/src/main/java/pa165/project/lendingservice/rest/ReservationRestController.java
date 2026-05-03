package pa165.project.lendingservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.facade.ReservationFacade;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation Management API")
public class ReservationRestController {

    private final ReservationFacade reservationFacade;

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationFacade.getAllReservations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        return reservationFacade.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get reservations by member ID")
    public ResponseEntity<List<ReservationDto>> getReservationsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(reservationFacade.getReservationsByMemberId(memberId));
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get reservations by book ID")
    public ResponseEntity<List<ReservationDto>> getReservationsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reservationFacade.getReservationsByBookId(bookId));
    }

    @PostMapping
    @Operation(summary = "Create reservation")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationFacade.createReservation(reservationDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reservation")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,
                                                            @RequestBody ReservationDto reservationDto) {
        reservationDto.setId(id);
        return ResponseEntity.ok(reservationFacade.updateReservation(reservationDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationFacade.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}