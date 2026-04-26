package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pa165.project.lendingservice.model.Loan;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LoanRepositoryTests {

    @Autowired
    private LoanRepository loanRepository;

    private Loan loan1;
    private Loan loan2;
    private Loan loan3;
    @BeforeEach
    public void setup() {
        loanRepository.deleteAll();

        loan1 = loanRepository.save(Loan.builder()
                .memberId(10L)
                .bookCopyId(100L)
                .loanDate(LocalDate.of(2026, 1, 1))
                .dueDate(LocalDate.of(2026, 1, 15))
                .status("ACTIVE")
                .build());

        loan2 = loanRepository.save(Loan.builder()
                .memberId(10L)
                .bookCopyId(101L)
                .loanDate(LocalDate.of(2026, 2, 1))
                .dueDate(LocalDate.of(2026, 2, 15))
                .returnDate(LocalDate.of(2026, 2, 10))
                .status("RETURNED")
                .build());

        loan3 = loanRepository.save(Loan.builder()
                .memberId(20L)
                .bookCopyId(102L)
                .loanDate(LocalDate.of(2026, 3, 1))
                .dueDate(LocalDate.of(2026, 3, 15))
                .status("ACTIVE")
                .build());
    }

    @Test
    public void findAll_seededData_returnsAllThreeLoans() {
        var result = loanRepository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    public void findById_existingId_returnsLoan() {
        var result = loanRepository.findById(loan1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(10L);
        assertThat(result.get().getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    public void findById_nonExistingId_returnsEmpty() {
        var result = loanRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void save_loanWithNullId_assignsGeneratedId() {
        var newLoan = Loan.builder()
                .memberId(30L)
                .bookCopyId(200L)
                .loanDate(LocalDate.of(2026, 4, 1))
                .dueDate(LocalDate.of(2026, 4, 15))
                .status("ACTIVE")
                .build();

        var saved = loanRepository.save(newLoan);

        assertThat(saved.getId()).isNotNull();
        assertThat(loanRepository.findById(saved.getId())).isPresent();
    }

    @Test
    public void countActiveLoansByMember_memberWithTwoActiveLoans_returnsOne() {
        var count = loanRepository.countActiveLoansByMemberId(10L);

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void countActiveLoansByMember_memberWithNoActiveLoans_returnsZero() {
        var count = loanRepository.countActiveLoansByMemberId(999L);

        assertThat(count).isZero();
    }

    @Test
    public void findByMember_existingMember_returnsOnlyHisLoans() {
        var result = loanRepository.findByMemberId(10L);

        assertThat(result).hasSize(2);
    }

    @Test
    public void findByMember_nonExistingMember_returnsEmptyList() {
        var result = loanRepository.findByMemberId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void findByBookCopyId_existingBookCopy_returnsMatchingLoan() {
        var result = loanRepository.findByBookCopyId(102L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMemberId()).isEqualTo(20L);
    }

    @Test
    public void findByBookCopyId_nonExistingBookCopy_returnsEmptyList() {
        var result = loanRepository.findByBookCopyId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void deleteById_existingId_removesLoan() {
        loanRepository.deleteById(loan2.getId());

        assertThat(loanRepository.findById(2L)).isEmpty();
        assertThat(loanRepository.findAll()).hasSize(2);
    }

    @Test
    public void deleteById_nonExistingId_doesNotChangeTotalCount() {
        loanRepository.deleteById(999L);

        assertThat(loanRepository.findAll()).hasSize(3);
    }
}
