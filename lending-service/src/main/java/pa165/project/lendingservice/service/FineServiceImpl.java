package pa165.project.lendingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pa165.project.lendingservice.model.Fine;
import pa165.project.lendingservice.repository.FineRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;

    @Override
    public Fine createFine(Fine fine) {
        return fineRepository.save(fine);
    }

    @Override
    public Optional<Fine> getFineById(Long id) {
        return fineRepository.findById(id);
    }

    @Override
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    @Override
    public List<Fine> getFinesByMemberId(Long memberId) {
        return fineRepository.findByMemberId(memberId);
    }

    @Override
    public Fine updateFine(Fine fine) {
        return fineRepository.save(fine);
    }

    @Override
    public void deleteFine(Long id) {
        fineRepository.deleteById(id);
    }
}