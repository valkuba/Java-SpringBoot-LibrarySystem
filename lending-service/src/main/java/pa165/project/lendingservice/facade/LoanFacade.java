package pa165.project.lendingservice.facade;

import pa165.project.lendingservice.dto.LoanDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanFacade {
    List<LoanDto> getAllLoans();
    Optional<LoanDto> getLoanById(Long id);
    LoanDto createLoan(LoanDto loanDto);
    LoanDto updateLoan(Long id, LoanDto loanDto);
    LoanDto returnLoan(Long id, LocalDate returnDate);
    void deleteLoan(Long id);
    List<LoanDto> searchLoan(Long memberId, Long bookCopyId);
}
