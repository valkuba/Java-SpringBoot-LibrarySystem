package pa165.project.lendingservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.mappers.LoanMapper;
import pa165.project.lendingservice.model.Loan;
import pa165.project.lendingservice.model.Loan;
import pa165.project.lendingservice.service.LoanService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LoanFacadeImplTests {

    @Mock
    private LoanService loanService;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanFacadeImpl loanFacade;

    @Test
    public void getAllLends_loansExist_returnsMappedDtos() {
        var loan1 = Loan.builder().id(1L).build();
        var loan2 = Loan.builder().id(2L).build();
        var loan3 = Loan.builder().id(3L).build();
        var dto1 = LoanDto.builder().id(1L).build();
        var dto2 = LoanDto.builder().id(2L).build();
        var dto3 = LoanDto.builder().id(3L).build();
        Mockito.when(loanService.getAllLoans()).thenReturn(List.of(loan1, loan2, loan3));
        Mockito.when(loanMapper.toDto(loan1)).thenReturn(dto1);
        Mockito.when(loanMapper.toDto(loan2)).thenReturn(dto2);
        Mockito.when(loanMapper.toDto(loan3)).thenReturn(dto3);

        var result = loanFacade.getAllLends();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(LoanDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getLendById_existingId_returnsMappedDto() {
        Mockito.when(loanMapper.toDto(Mockito.any())).thenReturn(LoanDto.builder().id(1L).build());
        Mockito.when(loanService.getLoanById(1L)).thenReturn(Optional.of(Loan.builder().id(1L).build()));

        var result = loanFacade.getLendById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    public void getLendById_nonExistingId_returnsEmpty() {
        Mockito.when(loanService.getLoanById(1L)).thenReturn(Optional.empty());

        var result =  loanFacade.getLendById(1L);
        assertThat(result).isEmpty();
    }

    @Test
    public void createLend_validDto_returnsMappedDto() {
        Mockito.when(loanMapper.toDto(Mockito.any())).thenReturn(LoanDto.builder().id(1L).build());
        Mockito.when(loanService.createLoan(Mockito.any())).thenReturn(Loan.builder().id(1L).build());

        var result = loanFacade.createLend(LoanDto.builder().id(1L).build());

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void updateLend_existingId_returnsMappedDto() {
        var date = LocalDate.now();
        Mockito.when(loanMapper.toDto(Mockito.any())).thenReturn(LoanDto.builder().id(1L).dueDate(date).build());
        Mockito.when(loanService.updateLoan(Mockito.eq(1L), Mockito.any())).thenReturn(Loan.builder().id(1L).dueDate(date).build());

        var result = loanFacade.updateLend(1L,LoanDto.builder().id(1L).build());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDueDate()).isEqualTo(date);
    }

    @Test
    public void deleteLend_existingId_callsServiceDeleteLoan() {
        loanFacade.deleteLend(1L);

        Mockito.verify(loanService).deleteLoan(1L);
    }

    @Test
    public void searchLends_withFilters_returnsMappedDtos() {
        var loan = Loan.builder().id(1L).memberId(1L).bookCopyId(2L).build();
        var dto = LoanDto.builder().id(1L).build();
        Mockito.when(loanService.searchLoans(1L,2L)).thenReturn(List.of(loan));
        Mockito.when(loanMapper.toDto(loan)).thenReturn(dto);

        var loans = loanFacade.searchLends(1L,2L);

        assertThat(loans).hasSize(1);
        assertThat(loans).extracting(LoanDto::getId).containsExactlyInAnyOrder(1L);
    }
}
