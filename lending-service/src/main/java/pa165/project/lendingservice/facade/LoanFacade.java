package pa165.project.lendingservice.facade;

import pa165.project.lendingservice.dto.LoanDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanFacade {
    List<LoanDto> getAllLends();
    Optional<LoanDto> getLendById(Long id);
    LoanDto createLend(LoanDto loanDto);
    LoanDto updateLend(Long id, LoanDto loanDto);
    LoanDto returnLend(Long id, LocalDate returnDate);
    void deleteLend(Long id);
    List<LoanDto> searchLends(Long memberId, Long bookCopyId);
}
