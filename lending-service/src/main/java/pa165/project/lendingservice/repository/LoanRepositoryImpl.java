package pa165.project.lendingservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.lendingservice.model.Loan;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    // we dont use JPA in the M1 so instead use HashMap
    private final Map<Long, Loan> loans = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public long countActiveLoansByMember(Long memberId) {
        return loans.values().stream()
                .filter(loan -> loan.getMemberId().equals(memberId))
                .filter(loan -> "ACTIVE".equals(loan.getStatus()))
                .count();
    }

    @Override
    public Loan save(Loan loan) {
        if(loan.getId() == null) {
            loan.setId(idGenerator.getAndIncrement());
        }
        loans.put(loan.getId(), loan);
        return loan;
    }

    @Override
    public void deleteById(Long id) {
        loans.remove(id);
    }

    @Override
    public List<Loan> findByMember(Long memberId) {
        return loans.values().stream()
                .filter(b -> b.getMemberId().equals(memberId))
                .toList();
    }

    @Override
    public List<Loan> findByBookCopyId(Long bookCopyId) {
        return loans.values().stream()
                .filter(b -> b.getBookCopyId().equals(bookCopyId))
                .toList();
    }
}
