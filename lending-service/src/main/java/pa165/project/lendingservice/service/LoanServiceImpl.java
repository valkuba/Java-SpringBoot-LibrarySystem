package pa165.project.lendingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pa165.project.lendingservice.config.LoanConstants;
import pa165.project.lendingservice.messaging.LoanMessageProducer;
import pa165.project.lendingservice.model.Fine;
import pa165.project.lendingservice.model.Loan;
import pa165.project.lendingservice.repository.FineRepository;
import pa165.project.lendingservice.repository.LoanRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final LoanMessageProducer loanMessageProducer;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan createLoan(Loan loan) {
        long activeLoans = loanRepository.countActiveLoansByMemberId(loan.getMemberId());
        if(activeLoans >= LoanConstants.MAX_ACTIVE_LOANS){
            throw new RuntimeException("Member has reached maximum active loans");
        }

        Loan savedLoan = loanRepository.save(loan);

        loanMessageProducer.sendLoanCreated(
                savedLoan.getId(),
                savedLoan.getMemberId(),
                savedLoan.getBookCopyId(),
                savedLoan.getLoanDate().toString()
        );

        return savedLoan;
    }

    @Override
    public Loan updateLoan(Long id, Loan loan) {
        loan.setId(id);
        return loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    public List<Loan> searchLoans(Long memberId, Long bookCopyId) {
        return loanRepository.findAll().stream()
                .filter(b -> memberId == null || b.getMemberId().equals(memberId))
                .filter(b -> bookCopyId == null || b.getBookCopyId().equals(bookCopyId))
                .toList();
    }

    @Override
    public Loan returnLoan(Long loanId, LocalDate actualReturnDate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setReturnDate(actualReturnDate);

        double fineAmount = calculateFine(loan.getDueDate(), actualReturnDate);

        if (fineAmount > 0) {
            Fine fine = Fine.builder()
                    .loanId(loan.getId())
                    .memberId(loan.getMemberId())
                    .amount(fineAmount)
                    .date(actualReturnDate)
                    .status("UNPAID")
                    .build();
            fineRepository.save(fine);

            loanMessageProducer.sendFineIssued(
                    loan.getId(),
                    loan.getMemberId(),
                    fine.getAmount(),
                    fine.getDate().toString()
            );
        }
        Loan saved = loanRepository.save(loan);

        loanMessageProducer.sendLoanReturned(
                saved.getId(),
                saved.getMemberId(),
                actualReturnDate.toString()
        );

        return saved;
    }

    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        // TODO: figure out the fine amount per day
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysLate * LoanConstants.FINE_PER_DAY;
        }

        return 0;
    }

}
