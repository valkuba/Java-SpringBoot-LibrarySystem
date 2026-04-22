package pa165.project.lendingservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.mappers.LoanMapper;
import pa165.project.lendingservice.service.LoanService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoanFacadeImpl implements LoanFacade {
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Override
    public List<LoanDto> getAllLoans() {
        return  loanService.getAllLoans().stream()
                .map(loanMapper::toDto)
                .toList();
    }

    @Override
    public Optional<LoanDto> getLoanById(Long id) {
        return loanService.getLoanById(id)
                .map(loanMapper::toDto);
    }

    @Override
    public LoanDto createLoan(LoanDto loanDto) {
        return loanMapper.toDto(loanService.createLoan(loanMapper.toModel(loanDto)));
    }

    @Override
    public LoanDto updateLoan(Long id, LoanDto loanDto) {
        return loanMapper.toDto(loanService.updateLoan(id, loanMapper.toModel(loanDto)));
    }

    @Override
    public LoanDto returnLoan(Long id, LocalDate returnDate) {
        return loanMapper.toDto(loanService.returnLoan(id, returnDate));
    }

    @Override
    public void deleteLoan(Long id){
        loanService.deleteLoan(id);
    }

    @Override
    public List<LoanDto> searchLoan(Long memberId, Long bookCopyId) {
        return loanService.searchLoans(memberId, bookCopyId).stream()
                .map(loanMapper::toDto)
                .toList();
    }

}
