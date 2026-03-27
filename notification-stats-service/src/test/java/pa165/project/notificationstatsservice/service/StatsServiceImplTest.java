package pa165.project.notificationstatsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.notificationstatsservice.model.Stats;
import pa165.project.notificationstatsservice.repository.StatsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsServiceImpl statsServiceImpl;

    @Test
    void getAllStats() {
        List<Stats> stats = List.of(Stats.builder().bookTitle("Last wish").returned(true).build());
        when(statsRepository.findAll()).thenReturn(stats); // mock the repository to return the list of stats

        List<Stats> result = statsServiceImpl.getAllStats();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookTitle()).isEqualTo("Last wish");
    }

    @Test
    void getTopNBooks_shouldCountBorrowingsPerBook() {
        List<Stats> stats = List.of(
                Stats.builder().bookTitle("Blood of elves").returned(true).build(),
                Stats.builder().bookTitle("Blood of elves").returned(false).build(),
                Stats.builder().bookTitle("Last wish").returned(true).build()
        );

        when(statsRepository.findAll()).thenReturn(stats);

        Map<String, Long> result = statsServiceImpl.getTopNBooks(10); // get top 10 books by borrow count

        assertThat(result.get("Blood of elves")).isEqualTo(2L); // check if the result contains the correct number of borrowings for the book 
        assertThat(result.get("Last wish")).isEqualTo(1L);
    }

    @Test
    void getTopNBooks_shouldRespectLimit() {
        List<Stats> stats = List.of(
                Stats.builder().bookTitle("Last wish").returned(true).build(),
                Stats.builder().bookTitle("Last wish").returned(true).build(),
                Stats.builder().bookTitle("Sword of destiny").returned(true).build(),
                Stats.builder().bookTitle("Blood of elves").returned(true).build()
        );
        when(statsRepository.findAll()).thenReturn(stats);

        Map<String, Long> result = statsServiceImpl.getTopNBooks(2);

        assertThat(result).hasSize(2);
    }

    @Test
    void getTopNBooks_shouldReturnInDescendingOrder() {
        List<Stats> stats = List.of(
                Stats.builder().bookTitle("Blood of elves").returned(true).build(),
                Stats.builder().bookTitle("Last wish").returned(true).build(),
                Stats.builder().bookTitle("Last wish").returned(true).build()
        );
        when(statsRepository.findAll()).thenReturn(stats);

        Map<String, Long> result = statsServiceImpl.getTopNBooks(10);
        List<String> keys = new ArrayList<>(result.keySet());

        assertThat(keys.get(0)).isEqualTo("Last wish"); // most favourite should be first
    }

    @Test
    void getTopNBooks_withEmptyData_shouldReturnEmpty() {
        when(statsRepository.findAll()).thenReturn(List.of());
        Map<String, Long> result = statsServiceImpl.getTopNBooks(5);

        assertThat(result).hasSize(0);
    }

}