package pa165.project.notificationstatsservice.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.mapper.StatsMapper;
import pa165.project.notificationstatsservice.model.Stats;
import pa165.project.notificationstatsservice.service.StatsService;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsFacadeImplTest {

    @Mock
    private StatsService statsService;

    @Mock
    private StatsMapper statsMapper;

    @InjectMocks
    private StatsFacadeImpl statsFacade;

    @Test
    void getAllStats_shouldMapAndReturnAll() {
        Stats model = Stats.builder().id(1L).bookTitle("Blood of elves").returned(true).build();
        StatsDto dto = StatsDto.builder().id(1L).bookTitle("Blood of elves").returned(true).build();

        when(statsService.getAllStats()).thenReturn(List.of(model));
        when(statsMapper.toDto(model)).thenReturn(dto);

        List<StatsDto> result = statsFacade.getAllStats();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookTitle()).isEqualTo("Blood of elves");
        verify(statsService).getAllStats();
    }

    @Test
    void getTopNBooks_shouldReturnPopularBookDtos() {
        Map<String, Long> serviceResult = new LinkedHashMap<>();
        serviceResult.put("Blood of elves", 5L);
        serviceResult.put("Sword of destiny", 3L);

        when(statsService.getTopNBooks(2)).thenReturn(serviceResult);

        List<PopularBookDto> result = statsFacade.getTopNBooks(2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getBookTitle()).isEqualTo("Blood of elves");
        assertThat(result.get(0).getBorrowCount()).isEqualTo(5L);
        assertThat(result.get(1).getBookTitle()).isEqualTo("Sword of destiny");
    }

    @Test
    void getTopNBooks_withEmptyData_shouldReturnEmpty() {
        when(statsService.getTopNBooks(5)).thenReturn(new LinkedHashMap<>());

        List<PopularBookDto> result = statsFacade.getTopNBooks(5);

        assertThat(result).isEmpty();
    }
}