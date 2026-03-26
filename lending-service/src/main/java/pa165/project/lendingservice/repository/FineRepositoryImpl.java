package pa165.project.lendingservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.lendingservice.model.Fine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FineRepositoryImpl implements FineRepository {

    private final List<Fine> fines = new ArrayList<>();

    @Override
    public Fine save(Fine fine) {
        fines.add(fine);
        return fine;
    }

    @Override
    public Optional<Fine> findById(Long id) {
        return fines.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Fine> findAll() {
        return new ArrayList<>(fines);
    }

    @Override
    public List<Fine> findByMemberId(Long memberId) {
        return fines.stream()
                .filter(f -> f.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public Fine update(Fine fine) {
        findById(fine.getId()).ifPresent(old -> {
            fines.remove(old);
            fines.add(fine);
        });
        return fine;
    }

    @Override
    public void deleteById(Long id) {
        fines.removeIf(f -> f.getId().equals(id));
    }
}