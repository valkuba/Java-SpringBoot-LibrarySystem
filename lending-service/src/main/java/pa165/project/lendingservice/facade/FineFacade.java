package pa165.project.lendingservice.facade;

import pa165.project.lendingservice.dto.FineDto;

import java.util.List;
import java.util.Optional;

public interface FineFacade {
    FineDto createFine(FineDto fineDto);
    Optional<FineDto> getFineById(Long id);
    List<FineDto> getAllFines();
    List<FineDto> getFinesByMemberId(Long memberId);
    FineDto updateFine(FineDto fineDto);
    void deleteFine(Long id);
}