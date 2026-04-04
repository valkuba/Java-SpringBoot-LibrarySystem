package pa165.project.lendingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pa165.project.lendingservice.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long>
{
    List<Loan> findAll();
    Optional<Loan> findById(Long id);
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.memberId = :memberId AND l.status = 'ACTIVE'")
    long countActiveLoansByMemberId(@Param("memberId") Long memberId);
    Loan save(Loan loan);
    void deleteById(Long id);
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByBookCopyId(Long bookCopyId);
}