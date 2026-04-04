package pa165.project.lendingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pa165.project.lendingservice.model.Fine;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberId(Long memberId);
    List<Fine> findByStatus(String status);
}