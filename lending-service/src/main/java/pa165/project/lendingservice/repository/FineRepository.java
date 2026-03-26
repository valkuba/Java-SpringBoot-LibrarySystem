package pa165.project.lendingservice.repository;

import pa165.project.lendingservice.model.Fine;

import java.util.List;
import java.util.Optional;

public interface FineRepository {
    Fine save(Fine fine);                    // create
    Optional<Fine> findById(Long id);        // read by id
    List<Fine> findAll();                     // read all
    List<Fine> findByMemberId(Long memberId); // read by member
    Fine update(Fine fine);                   // update
    void deleteById(Long id);                // delete
}