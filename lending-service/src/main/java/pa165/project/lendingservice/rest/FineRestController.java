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

    @Operation(summary = "Get all fines")
    @GetMapping
    public List<FineDto> getAllFines() {
        return fineFacade.getAllFines();
    }

    @Operation(summary = "Get a fine by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<FineDto> getFineById(@PathVariable Long id) {
        return fineFacade.getFineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all fines for a specific member")
    @GetMapping("/member/{memberId}")
    public List<FineDto> getFinesByMember(@PathVariable Long memberId) {
        return fineFacade.getFinesByMemberId(memberId);
    }

    @Operation(summary = "Create a new fine")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FineDto createFine(@RequestBody FineDto fineDto) {
        return fineFacade.createFine(fineDto);
    }

    @Operation(summary = "Update an existing fine by ID")
    @PutMapping("/{id}")
    public FineDto updateFine(@PathVariable Long id, @RequestBody FineDto fineDto) {
        fineDto.setId(id);
        return fineFacade.updateFine(fineDto);
    }

    @Operation(summary = "Delete a fine by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFine(@PathVariable Long id) {
        fineFacade.deleteFine(id);
    }
}