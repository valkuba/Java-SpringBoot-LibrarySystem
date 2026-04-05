package pa165.project.lendingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pa165.project.lendingservice.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.memberId = :memberId AND l.status = 'ACTIVE'")
    long countActiveLoansByMemberId(@Param("memberId") Long memberId);

    List<Loan> findByMemberId(Long memberId);

    List<Loan> findByBookCopyId(Long bookCopyId);
}