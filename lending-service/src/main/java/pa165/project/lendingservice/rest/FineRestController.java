package pa165.project.lendingservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.facade.FineFacade;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@Tag(name = "Fine", description = "Fine Management API")
public class FineRestController {

    private final FineFacade fineFacade;

    @GetMapping
    @Operation(summary = "Get all fines")
    public ResponseEntity<List<FineDto>> getAllFines() {
        return ResponseEntity.ok(fineFacade.getAllFines());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get fine by ID")
    public ResponseEntity<FineDto> getFineById(@PathVariable Long id) {
        return fineFacade.getFineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get fines by member ID")
    public ResponseEntity<List<FineDto>> getFinesByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(fineFacade.getFinesByMemberId(memberId));
    }

    @PostMapping
    @Operation(summary = "Create fine")
    public ResponseEntity<FineDto> createFine(@RequestBody FineDto fineDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fineFacade.createFine(fineDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update fine")
    public ResponseEntity<FineDto> updateFine(@PathVariable Long id,
                                              @RequestBody FineDto fineDto) {
        fineDto.setId(id);
        return ResponseEntity.ok(fineFacade.updateFine(fineDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete fine")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineFacade.deleteFine(id);
        return ResponseEntity.noContent().build();
    }
}