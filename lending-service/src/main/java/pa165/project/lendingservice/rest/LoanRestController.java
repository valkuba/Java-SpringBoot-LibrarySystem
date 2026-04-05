package pa165.project.lendingservice.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.facade.LoanFacade;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "Loan Management API")
public class LoanRestController {
    private final LoanFacade loanFacade;

    @GetMapping
    @Operation(summary = "Get all loans")
    public ResponseEntity<List<LoanDto>> getAllLends() {
        return ResponseEntity.ok(loanFacade.getAllLends());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID")
    public ResponseEntity<LoanDto> getLendById(@PathVariable Long id) {
        return loanFacade.getLendById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new loan")
    public ResponseEntity<LoanDto> createBook(@Valid @RequestBody LoanDto loanDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanFacade.createLend(loanDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a loan")
    public ResponseEntity<LoanDto> updateLend(@PathVariable Long id,
                                              @Valid @RequestBody LoanDto loanDto) {
        return ResponseEntity.ok(loanFacade.updateLend(id, loanDto));
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Return a loan")
    public ResponseEntity<LoanDto> returnLoan(@PathVariable Long id,
                                              @RequestParam LocalDate returnDate) {
        return ResponseEntity.ok(loanFacade.returnLend(id, returnDate));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan")
    public ResponseEntity<Void> deleteLend(@PathVariable Long id) {
        loanFacade.deleteLend(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search loans by memberId or bookCopyId")
    public ResponseEntity<List<LoanDto>>  searchLends(@RequestParam(required = false) Long memberId,
                                                      @RequestParam(required = false) Long bookCopyId) {
        return  ResponseEntity.ok(loanFacade.searchLends(memberId, bookCopyId));
    }

}
