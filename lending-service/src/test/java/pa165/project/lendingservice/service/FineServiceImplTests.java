package pa165.project.lendingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.model.Fine;
import pa165.project.lendingservice.repository.FineRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FineServiceImplTests {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineServiceImpl fineService;

    @Test
    public void createFine_validFine_returnsCreatedFine() {
        var fine = Fine.builder()
                .id(1L)
                .memberId(10L)
                .loanId(100L)
                .amount(15.0)
                .status("UNPAID")
                .build();
        Mockito.when(fineRepository.save(fine)).thenReturn(fine);

        var createdFine = fineService.createFine(fine);

        assertThat(createdFine).isNotNull();
        assertThat(createdFine).isEqualTo(fine);
    }

    @Test
    public void getFineById_existingId_returnsFine() {
        Mockito.when(fineRepository.findById(1L)).thenReturn(Optional.of(Fine.builder()
                .id(1L)
                .status("UNPAID")
                .build()));

        var fine = fineService.getFineById(1L);

        assertThat(fine).isPresent();
        assertThat(fine.get().getId()).isEqualTo(1L);
        assertThat(fine.get().getStatus()).isEqualTo("UNPAID");
    }

    @Test
    public void getFineById_nonExistingId_returnsEmpty() {
        Mockito.when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        var fine = fineService.getFineById(1L);

        assertThat(fine).isEmpty();
    }

    @Test
    public void getAllFines_finesExist_returnsAllFines() {
        Mockito.when(fineRepository.findAll()).thenReturn(List.of(
                Fine.builder().id(1L).build(),
                Fine.builder().id(2L).build(),
                Fine.builder().id(3L).build()));

        var fines = fineService.getAllFines();

        assertThat(fines).hasSize(3);
        assertThat(fines).extracting(Fine::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getFinesByMemberId_memberWithFines_returnsHisFines() {
        Mockito.when(fineRepository.findByMemberId(1L)).thenReturn(List.of(
                Fine.builder().id(1L).memberId(1L).build(),
                Fine.builder().id(2L).memberId(1L).build(),
                Fine.builder().id(3L).memberId(1L).build()
        ));

        var fines = fineService.getFinesByMemberId(1L);

        assertThat(fines).hasSize(3);
        assertThat(fines).extracting(Fine::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getFinesByMemberId_nonExistingMember_returnsEmptyList() {
        Mockito.when(fineRepository.findByMemberId(1L)).thenReturn(List.of());

        var fines = fineService.getFinesByMemberId(1L);

        assertThat(fines).isEmpty();
    }

    @Test
    public void updateFine_existingFine_returnsUpdatedFine() {
        var fine = Fine.builder().id(1L).status("PAID").build();
        Mockito.when(fineRepository.save(fine)).thenReturn(fine);

        var updatedFine = fineService.updateFine(fine);

        assertThat(updatedFine).isEqualTo(fine);
    }

    @Test
    public void deleteFine_existingId_callsRepositoryDeleteById() {
        fineService.deleteFine(1L);

        Mockito.verify(fineRepository).deleteById(1L);
    }
}
