package pa165.project.lendingservice.service;

import pa165.project.lendingservice.model.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<Loan> getAllLoans();
    Optional<Loan> getLoanById(Long id);
    Loan createLoan(Loan loan);
    Loan updateLoan(Long id, Loan loan);
    void deleteLoan(Long id);
    List<Loan> searchLoans(Long memberId, Long bookCopyId);
    Loan returnLoan(Long loanId, LocalDate actualReturnDate);
    double calculateFine(LocalDate dueDate, LocalDate returnDate);
}
