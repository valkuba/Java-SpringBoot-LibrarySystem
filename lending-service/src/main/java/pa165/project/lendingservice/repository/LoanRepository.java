package pa165.project.lendingservice.repository;

import pa165.project.lendingservice.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository
{
    List<Loan> findAll();
    Optional<Loan> findById(Long id);
    long countActiveLoansByMember(Long memberId);
    Loan save(Loan loan);
    void deleteById(Long id);
    List<Loan> findByMember(Long memberId);
    List<Loan> findByBookCopyId(Long bookCopyId);
}