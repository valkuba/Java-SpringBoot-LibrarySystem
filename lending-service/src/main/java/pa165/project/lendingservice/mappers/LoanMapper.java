package pa165.project.lendingservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.model.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanDto toDto(Loan loan);
    Loan toModel(LoanDto loanDto);
}
