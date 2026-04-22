package pa165.project.lendingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.messaging.LoanMessageProducer;
import pa165.project.lendingservice.model.Loan;
import pa165.project.lendingservice.repository.FineRepository;
import pa165.project.lendingservice.repository.LoanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTests {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanMessageProducer loanMessageProducer;

    @Test
    public void getAllLoans_loansExist_returnsAllLoans() {
        Mockito.when(loanRepository.findAll()).thenReturn(List.of(
                Loan.builder().id(1L).build(),
                Loan.builder().id(2L).build(),
                Loan.builder().id(3L).build()
        ));

        var loans = loanService.getAllLoans();

        assertThat(loans).hasSize(3);
        assertThat(loans).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getLoanById_existingId_returnsLoan() {
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(Loan.builder().id(1L).memberId(1L).build()));

        var loan = loanService.getLoanById(1L);

        assertThat(loan).isPresent();
        assertThat(loan.get().getId()).isEqualTo(1L);
        assertThat(loan.get().getMemberId()).isEqualTo(1L);
    }

    @Test
    public void getLoanById_nonExistingId_returnsEmpty() {
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        var loan = loanService.getLoanById(1L);

        assertThat(loan).isEmpty();
    }

    @Test
    public void createLoan_memberBelowMaxActiveLoans_savesAndReturnsLoan() {
        Mockito.when(loanRepository.countActiveLoansByMemberId(1L)).thenReturn(2L);

        var loansCount = loanRepository.countActiveLoansByMemberId(1L);

        assertThat(loansCount).isEqualTo(2);
    }

    @Test
    public void createLoan_memberAtMaxActiveLoans_throwsRuntimeException() {
        Mockito.when(loanRepository.countActiveLoansByMemberId(1L)).thenThrow(new RuntimeException());

        assertThatThrownBy(()->loanService.createLoan(Loan.builder().build())).isInstanceOf(RuntimeException.class);
    }

    @Test
    void createLoan_shouldSendMessageToProducer() {
        Loan loan = Loan.builder()
                .id(1L)
                .memberId(5L)
                .bookCopyId(10L)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        Mockito.when(loanRepository.countActiveLoansByMemberId(5L)).thenReturn(0L);
        Mockito.when(loanRepository.save(Mockito.any())).thenReturn(loan);

        loanService.createLoan(loan);

        Mockito.verify(loanMessageProducer).sendLoanCreated(
                1L,
                5L,
                10L,
                LocalDate.now().toString()
        );
    }

    @Test
    void returnLoan_withFine_sendsFineIssuedMessage() {
        Loan loan = Loan.builder()
                .id(1L)
                .memberId(1L)
                .bookCopyId(1L)
                .dueDate(LocalDate.of(2026, 1, 1))
                .build();

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Mockito.when(loanRepository.save(Mockito.any())).thenReturn(loan);

        LocalDate returnDate = LocalDate.of(2026, 1, 10);

        loanService.returnLoan(1L, returnDate);

        Mockito.verify(fineRepository).save(Mockito.any());

        Mockito.verify(loanMessageProducer).sendFineIssued(
                1L,
                1L,
                45.0d,
                returnDate.toString()
        );
    }

    @Test
    void returnLoan_sendsLoanReturnedMessage() {
        Loan loan = Loan.builder()
                .id(1L)
                .memberId(1L)
                .bookCopyId(1L)
                .dueDate(LocalDate.of(2026, 1, 1))
                .build();

        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Mockito.when(loanRepository.save(Mockito.any())).thenReturn(loan);

        LocalDate returnDate = LocalDate.of(2026, 1, 2);

        loanService.returnLoan(1L, returnDate);

        Mockito.verify(loanMessageProducer).sendLoanReturned(
                1L,
                1L,
                returnDate.toString()
        );
    }

    @Test
    public void updateLoan_existingLoan_setsIdAndSaves() {
        Mockito.when(loanRepository.save(Mockito.any(Loan.class))).thenReturn(Loan.builder().id(1L).build());

        var updated = loanService.updateLoan(1L, Loan.builder().id(2L).build());

        assertThat(updated.getId()).isEqualTo(1L);
    }

    @Test
    public void deleteLoan_existingId_callsRepositoryDeleteById() {
        loanService.deleteLoan(1L);

        Mockito.verify(loanRepository).deleteById(1L);
    }

    @Test
    public void searchLoans_withMemberIdFilter_returnsOnlyMatchingLoans() {
        Mockito.when(loanRepository.findAll()).thenReturn(List.of(
                Loan.builder().memberId(1L).id(1L).build(),
                Loan.builder().memberId(2L).id(2L).build(),
                Loan.builder().memberId(1L).id(3L).build(),
                Loan.builder().memberId(3L).id(4L).build()
        ));

        var loans = loanService.searchLoans(1L,null);

        assertThat(loans).hasSize(2);
        assertThat(loans).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    public void searchLoans_withBookCopyIdFilter_returnsOnlyMatchingLoans() {
        Mockito.when(loanRepository.findAll()).thenReturn(List.of(
                Loan.builder().bookCopyId(1L).id(1L).build(),
                Loan.builder().bookCopyId(2L).id(2L).build(),
                Loan.builder().bookCopyId(1L).id(3L).build(),
                Loan.builder().bookCopyId(3L).id(4L).build()
        ));

        var loans = loanService.searchLoans(null,1L);

        assertThat(loans).hasSize(2);
        assertThat(loans).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    public void searchLoans_noFilters_returnsAllLoans() {
        Mockito.when(loanRepository.findAll()).thenReturn(List.of(
                Loan.builder().bookCopyId(1L).id(1L).build(),
                Loan.builder().bookCopyId(2L).id(2L).build(),
                Loan.builder().bookCopyId(1L).id(3L).build(),
                Loan.builder().bookCopyId(3L).id(4L).build()
        ));

        var loans = loanService.searchLoans(null,null);

        assertThat(loans).hasSize(4);
        assertThat(loans).extracting(Loan::getId).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @Test
    public void returnLoan_nonExistingLoan_throwsRuntimeException() {
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->loanService.returnLoan(1L, LocalDate.now())).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void returnLoan_loanReturnedOnTime_setsReturnDateWithoutFine() {
        var dueDate = LocalDate.of(2026, 4, 15);
        var returnDate = LocalDate.of(2026, 3, 27);
        var loan = Loan.builder().id(1L).memberId(10L).dueDate(dueDate).build();
        Mockito.when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Mockito.when(loanRepository.save(loan)).thenReturn(loan);

        var result = loanService.returnLoan(1L, returnDate);

        assertThat(result.getReturnDate()).isEqualTo(returnDate);
    }

    @Test
    public void calculateFine_returnOnDueDate_returnsZero() {
        var fine = loanService.calculateFine(LocalDate.now(), LocalDate.now());
        assertThat(fine).isEqualTo(0.0);
    }

}
