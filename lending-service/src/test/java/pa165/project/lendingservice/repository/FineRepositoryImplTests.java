package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pa165.project.lendingservice.model.Fine;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FineRepositoryImplTests {
    @Autowired
    private FineRepositoryImpl fineRepository;

    @BeforeEach
    public void setup() {
        fineRepository.findAll().forEach(f -> fineRepository.deleteById(f.getId()));

        fineRepository.save(Fine.builder()
                .id(1L)
                .memberId(10L)
                .loanId(100L)
                .date(LocalDate.of(2026, 1, 15))
                .amount(15.0)
                .status("UNPAID")
                .build());

        fineRepository.save(Fine.builder()
                .id(2L)
                .memberId(10L)
                .loanId(101L)
                .date(LocalDate.of(2026, 2, 20))
                .amount(25.0)
                .status("PAID")
                .build());

        fineRepository.save(Fine.builder()
                .id(3L)
                .memberId(20L)
                .loanId(102L)
                .date(LocalDate.of(2026, 3, 5))
                .amount(10.0)
                .status("UNPAID")
                .build());
    }

    @Test
    public void save_newFine_returnsAndPersistsFine() {
        var newFine = Fine.builder()
                .id(99L)
                .memberId(30L)
                .loanId(200L)
                .date(LocalDate.of(2026, 4, 1))
                .amount(50.0)
                .status("UNPAID")
                .build();

        var saved = fineRepository.save(newFine);

        assertThat(saved).isEqualTo(newFine);
        assertThat(fineRepository.findById(99L)).isPresent();
    }

    @Test
    public void findById_existingId_returnsFine() {
        var result = fineRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(10L);
        assertThat(result.get().getAmount()).isEqualTo(15.0);
    }

    @Test
    public void findById_nonExistingId_returnsEmpty() {
        var result = fineRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void findAll_seededData_returnsAllThreeFines() {
        var result = fineRepository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Fine::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void findByMemberId_memberWithMultipleFines_returnsOnlyHisFines() {
        var result = fineRepository.findByMemberId(10L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Fine::getId).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    public void findByMemberId_nonExistingMember_returnsEmptyList() {
        var result = fineRepository.findByMemberId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void update_existingFine_updatesAmountAndStatus() {
        var updated = Fine.builder()
                .id(1L)
                .memberId(10L)
                .loanId(100L)
                .date(LocalDate.of(2026, 1, 15))
                .amount(99.0)
                .status("PAID")
                .build();

        var result = fineRepository.update(updated);

        assertThat(result.getAmount()).isEqualTo(99.0);
        assertThat(result.getStatus()).isEqualTo("PAID");
        var foundUpdated = fineRepository.findById(1L);
        assertThat(foundUpdated).isPresent();
        assertThat(foundUpdated.get().getAmount()).isEqualTo(99.0);
    }

    @Test
    public void update_nonExistingFine_doesNotChangeTotalCount() {
        var nonExisting = Fine.builder()
                .id(999L)
                .memberId(10L)
                .loanId(100L)
                .amount(50.0)
                .status("UNPAID")
                .build();

        fineRepository.update(nonExisting);

        assertThat(fineRepository.findAll()).hasSize(3);
    }

    @Test
    public void deleteById_existingId_removesFineFromRepository() {
        fineRepository.deleteById(2L);

        assertThat(fineRepository.findById(2L)).isEmpty();
        assertThat(fineRepository.findAll()).hasSize(2);
    }
}
