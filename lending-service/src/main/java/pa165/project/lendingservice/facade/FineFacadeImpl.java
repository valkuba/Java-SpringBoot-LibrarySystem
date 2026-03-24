package pa165.project.lendingservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.mappers.FineMapper;
import pa165.project.lendingservice.model.Fine;
import pa165.project.lendingservice.service.FineService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FineFacadeImpl implements FineFacade {

    private final FineService fineService;
    private final FineMapper fineMapper;

    @Override
    public FineDto createFine(FineDto fineDto) {
        Fine fine = fineMapper.toModel(fineDto);
        Fine saved = fineService.createFine(fine);
        return fineMapper.toDto(saved);
    }

    @Override
    public Optional<FineDto> getFineById(Long id) {
        return fineService.getFineById(id).map(fineMapper::toDto);
    }

    @Override
    public List<FineDto> getAllFines() {
        return fineService.getAllFines().stream()
                .map(fineMapper::toDto)
                .toList();
    }

    @Override
    public List<FineDto> getFinesByMemberId(Long memberId) {
        return fineService.getFinesByMemberId(memberId).stream()
                .map(fineMapper::toDto)
                .toList();
    }

    @Override
    public FineDto updateFine(FineDto fineDto) {
        Fine fine = fineMapper.toModel(fineDto);
        Fine updated = fineService.updateFine(fine);
        return fineMapper.toDto(updated);
    }

    @Override
    public void deleteFine(Long id) {
        fineService.deleteFine(id);
    }
}