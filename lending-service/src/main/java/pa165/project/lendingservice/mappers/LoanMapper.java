package pa165.project.lendingservice.mappers;

import org.mapstruct.Mapper;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.model.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanDto toDto(Loan loan);
    Loan toModel(LoanDto loanDto);
}
