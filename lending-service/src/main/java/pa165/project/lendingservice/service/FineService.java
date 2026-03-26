package pa165.project.lendingservice.service;

import pa165.project.lendingservice.model.Fine;

import java.util.List;
import java.util.Optional;

public interface FineService {
    Fine createFine(Fine fine);
    Optional<Fine> getFineById(Long id);
    List<Fine> getAllFines();
    List<Fine> getFinesByMemberId(Long memberId);
    Fine updateFine(Fine fine);
    void deleteFine(Long id);
}