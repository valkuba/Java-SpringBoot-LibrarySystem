package pa165.project.lendingservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.mappers.FineMapper;
import pa165.project.lendingservice.model.Fine;
import pa165.project.lendingservice.service.FineService;
import pa165.project.lendingservice.service.FineServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FineFacadeImplTests {

    @Mock
    private FineService fineService;

    @Mock
    private FineMapper fineMapper;

    @InjectMocks
    private FineFacadeImpl fineFacade;

    @Test
    public void createFine_validDto_returnsMappedDto() {
        Mockito.when(fineMapper.toDto(Mockito.any())).thenReturn(FineDto.builder().id(1L).build());
        Mockito.when(fineService.createFine(Mockito.any())).thenReturn(Fine.builder().id(1L).build());

        var createdDto = fineFacade.createFine(FineDto.builder().id(1L).build());
        assertThat(createdDto.getId()).isEqualTo(1L);
    }

    @Test
    public void getFineById_existingId_returnsMappedDto() {
        Mockito.when(fineMapper.toDto(Mockito.any())).thenReturn(FineDto.builder().id(1L).build());
        Mockito.when(fineService.getFineById(1L)).thenReturn(Optional.of(Fine.builder().id(1L).build()));

        var dto = fineFacade.getFineById(1L);

        assertThat(dto).isPresent();
        assertThat(dto.get().getId()).isEqualTo(1L);
    }

    @Test
    public void getFineById_nonExistingId_returnsEmpty() {
        Mockito.when(fineService.getFineById(1L)).thenReturn(Optional.empty());

        var dto = fineFacade.getFineById(1L);

        assertThat(dto).isEmpty();
    }

    @Test
    public void getAllFines_finesExist_returnsMappedDtos() {
        var fine1 = Fine.builder().id(1L).build();
        var fine2 = Fine.builder().id(2L).build();
        var fine3 = Fine.builder().id(3L).build();
        var dto1 = FineDto.builder().id(1L).build();
        var dto2 = FineDto.builder().id(2L).build();
        var dto3 = FineDto.builder().id(3L).build();
        Mockito.when(fineService.getAllFines()).thenReturn(List.of(fine1, fine2, fine3));
        Mockito.when(fineMapper.toDto(fine1)).thenReturn(dto1);
        Mockito.when(fineMapper.toDto(fine2)).thenReturn(dto2);
        Mockito.when(fineMapper.toDto(fine3)).thenReturn(dto3);

        var dtos = fineFacade.getAllFines();

        assertThat(dtos).hasSize(3);
        assertThat(dtos).extracting(FineDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getFinesByMemberId_memberWithFines_returnsMappedDtos() {
        var fine1 = Fine.builder().id(1L).build();
        var fine3 = Fine.builder().id(3L).build();
        var dto1 = FineDto.builder().id(1L).build();
        var dto3 = FineDto.builder().id(3L).build();
        Mockito.when(fineService.getFinesByMemberId(1L)).thenReturn(List.of(fine1, fine3));
        Mockito.when(fineMapper.toDto(fine1)).thenReturn(dto1);
        Mockito.when(fineMapper.toDto(fine3)).thenReturn(dto3);

        var dtos = fineFacade.getFinesByMemberId(1L);
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(FineDto::getId).containsExactlyInAnyOrder(1L, 3L);

    }

    @Test
    public void updateFine_existingFine_returnsMappedDto() {
        var date  = LocalDate.now();
        Mockito.when(fineMapper.toDto(Mockito.any())).thenReturn(FineDto.builder().id(1L).date(date).build());
        Mockito.when(fineService.updateFine(Mockito.any())).thenReturn(Fine.builder().id(1L).date(date).build());

        var updated =  fineFacade.updateFine(FineDto.builder().id(1L).date(date).build());

        assertThat(updated.getId()).isEqualTo(1L);
        assertThat(updated.getDate()).isEqualTo(date);
    }

    @Test
    public void deleteFine_existingId_callsServiceDeleteFine() {
        fineFacade.deleteFine(1L);
        Mockito.verify(fineService).deleteFine(1L);
    }
}
