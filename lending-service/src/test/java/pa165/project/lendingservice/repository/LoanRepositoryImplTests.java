package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pa165.project.lendingservice.model.Loan;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LoanRepositoryImplTests {

    @Autowired
    private LoanRepositoryImpl loanRepository;

    @BeforeEach
    public void setup() {
        loanRepository.findAll().forEach(l -> loanRepository.deleteById(l.getId()));

        loanRepository.save(Loan.builder()
                .id(1L)
                .memberId(10L)
                .bookCopyId(100L)
                .loanDate(LocalDate.of(2026, 1, 1))
                .dueDate(LocalDate.of(2026, 1, 15))
                .status("ACTIVE")
                .build());

        loanRepository.save(Loan.builder()
                .id(2L)
                .memberId(10L)
                .bookCopyId(101L)
                .loanDate(LocalDate.of(2026, 2, 1))
                .dueDate(LocalDate.of(2026, 2, 15))
                .returnDate(LocalDate.of(2026, 2, 10))
                .status("RETURNED")
                .build());

        loanRepository.save(Loan.builder()
                .id(3L)
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
        assertThat(result).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void findById_existingId_returnsLoan() {
        var result = loanRepository.findById(1L);

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
    public void save_loanWithExistingId_overwritesLoan() {
        var updated = Loan.builder()
                .id(1L)
                .memberId(10L)
                .bookCopyId(100L)
                .loanDate(LocalDate.of(2026, 1, 1))
                .dueDate(LocalDate.of(2026, 1, 15))
                .status("RETURNED")
                .build();

        loanRepository.save(updated);

        var foundUpdated = loanRepository.findById(1L);
        assertThat(foundUpdated).isPresent();
        assertThat(foundUpdated.get().getStatus()).isEqualTo("RETURNED");
        assertThat(loanRepository.findAll()).hasSize(3);
    }

    @Test
    public void countActiveLoansByMember_memberWithTwoActiveLoans_returnsOne() {
        var count = loanRepository.countActiveLoansByMember(10L);

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void countActiveLoansByMember_memberWithNoActiveLoans_returnsZero() {
        var count = loanRepository.countActiveLoansByMember(999L);

        assertThat(count).isZero();
    }

    @Test
    public void findByMember_existingMember_returnsOnlyHisLoans() {
        var result = loanRepository.findByMember(10L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    public void findByMember_nonExistingMember_returnsEmptyList() {
        var result = loanRepository.findByMember(999L);

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
        loanRepository.deleteById(2L);

        assertThat(loanRepository.findById(2L)).isEmpty();
        assertThat(loanRepository.findAll()).hasSize(2);
    }

    @Test
    public void deleteById_nonExistingId_doesNotChangeTotalCount() {
        loanRepository.deleteById(999L);

        assertThat(loanRepository.findAll()).hasSize(3);
    }
}
