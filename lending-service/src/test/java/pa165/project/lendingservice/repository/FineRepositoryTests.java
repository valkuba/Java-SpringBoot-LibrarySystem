package pa165.project.lendingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pa165.project.lendingservice.model.Fine;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FineRepositoryTests {
    @Autowired
    private FineRepository fineRepository;

    private Fine fine1;
    private Fine fine2;
    private Fine fine3;

    @BeforeEach
    void setup() {
        fineRepository.deleteAll();

        fine1 = fineRepository.save(Fine.builder()
                .memberId(10L)
                .loanId(100L)
                .date(LocalDate.of(2026, 1, 15))
                .amount(15.0)
                .status("UNPAID")
                .build());

        fine2 = fineRepository.save(Fine.builder()
                .memberId(10L)
                .loanId(101L)
                .date(LocalDate.of(2026, 2, 20))
                .amount(25.0)
                .status("PAID")
                .build());

        fine3 = fineRepository.save(Fine.builder()
                .memberId(20L)
                .loanId(102L)
                .date(LocalDate.of(2026, 3, 5))
                .amount(10.0)
                .status("UNPAID")
                .build());
    }

    @Test
    void save_newFine_persistsFine() {
        var newFine = Fine.builder()
                .memberId(30L)
                .loanId(200L)
                .date(LocalDate.of(2026, 4, 1))
                .amount(50.0)
                .status("UNPAID")
                .build();

        var saved = fineRepository.save(newFine);

        assertThat(saved.getId()).isNotNull();
        assertThat(fineRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findById_existingId_returnsFine() {
        var result = fineRepository.findById(fine1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(10L);
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        var result = fineRepository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsAllFines() {
        var result = fineRepository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    void findByMemberId_returnsCorrectFines() {
        var result = fineRepository.findByMemberId(10L);

        assertThat(result).hasSize(2);
    }

    @Test
    void findByMemberId_nonExisting_returnsEmpty() {
        var result = fineRepository.findByMemberId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void update_existingFine_updatesFields() {
        fine1.setAmount(99.0);
        fine1.setStatus("PAID");

        var updated = fineRepository.save(fine1);

        assertThat(updated.getAmount()).isEqualTo(99.0);
        assertThat(updated.getStatus()).isEqualTo("PAID");
    }

    @Test
    void deleteById_existingId_removesFine() {
        fineRepository.deleteById(fine2.getId());

        assertThat(fineRepository.findById(fine2.getId())).isEmpty();
        assertThat(fineRepository.findAll()).hasSize(2);
    }
}
