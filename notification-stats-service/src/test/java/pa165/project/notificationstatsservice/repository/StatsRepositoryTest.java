package pa165.project.notificationstatsservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pa165.project.notificationstatsservice.model.Stats;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class StatsRepositoryTest {
    @Autowired
    private StatsRepository statsRepository;

    @BeforeEach
    void setup() {
        statsRepository.deleteAll();

            Stats stat1 = new Stats();
            stat1.setBookTitle("Project Hail Mary");
            stat1.setBookId(1L);
            stat1.setReturned(true);
            statsRepository.save(stat1);

            Stats stat2 = new Stats();
            stat2.setBookTitle("Project Hail Mary");
            stat2.setBookId(1L);
            stat2.setReturned(false);
            statsRepository.save(stat2);

            Stats stat3 = new Stats();
            stat3.setBookTitle("Donnie Darko");
            stat3.setBookId(2L);
            stat3.setReturned(true);
            statsRepository.save(stat3);

            Stats stat4 = new Stats();
            stat4.setBookTitle("Animal Farm");
            stat4.setBookId(3L);
            stat4.setReturned(false);
            statsRepository.save(stat4);
    }

    // --- findByReturned ---

    @Test
    void findByReturned_True_ReturnsOnlyReturnedStats() {
        List<Stats> result = statsRepository.findByReturned(true);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Stats::getReturned).containsOnly(true);
        assertThat(result).extracting(Stats::getBookTitle)
                .containsExactlyInAnyOrder("Project Hail Mary", "Donnie Darko");
    }

    @Test
    void findByReturned_False_ReturnsOnlyActiveLoans() {
        List<Stats> result = statsRepository.findByReturned(false);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Stats::getReturned).containsOnly(false);
        assertThat(result).extracting(Stats::getBookTitle)
                .containsExactlyInAnyOrder("Project Hail Mary", "Animal Farm");
    }

    @Test
    void findByReturned_NoMatchingRecords_ReturnsEmptyList() {
        statsRepository.deleteAll();

        statsRepository.save(Stats.builder()
                .bookId(1L)
                .bookTitle("Project Hail Mary")
                .returned(true)
                .build());

        List<Stats> result = statsRepository.findByReturned(false);

        assertThat(result).isEmpty();
    }


    @Test
    void findByBookTitle_TitleExists_ReturnsAllMatchingStats() {
        List<Stats> result = statsRepository.findByBookTitle("Project Hail Mary");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Stats::getBookTitle).containsOnly("Project Hail Mary");
    }

    @Test
    void findByBookTitle_TitleExists_ContainsBothReturnedAndActive() {
        List<Stats> result = statsRepository.findByBookTitle("Project Hail Mary");

        assertThat(result).extracting(Stats::getReturned)
                .containsExactlyInAnyOrder(true, false);
    }

    @Test
    void findByBookTitle_TitleExistsOnce_ReturnsSingleRecord() {
        List<Stats> result = statsRepository.findByBookTitle("Donnie Darko");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookTitle()).isEqualTo("Donnie Darko");
        assertThat(result.get(0).getBookId()).isEqualTo(2L);
    }

    @Test
    void findByBookTitle_TitleNotExists_ReturnsEmptyList() {
        List<Stats> result = statsRepository.findByBookTitle("Nonexistent Book");

        assertThat(result).isEmpty();
    }

    @Test
    void findByBookTitle_IsCaseSensitive_ReturnsEmptyList() {
        //case-sensitive
        List<Stats> result = statsRepository.findByBookTitle("project hail mary");

        assertThat(result).isEmpty();
    }

    @Test
    void findBookBorrowCounts_ReturnsCorrectCountsOrderedDesc() {
        List<Object[]> result = statsRepository.findBookBorrowCounts();

        assertThat(result).isNotEmpty();
        Object[] topBook = result.get(0);
        assertThat(topBook[0]).isEqualTo("Project Hail Mary");
        assertThat((Long) topBook[1]).isEqualTo(2L);
    }
    

    @Test
    void save_ValidStats_PersistsWithGeneratedId() {
        Stats stats = Stats.builder()
                .bookId(99L)
                .bookTitle("New Book")
                .returned(false)
                .build();

        Stats saved = statsRepository.save(stats);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBookTitle()).isEqualTo("New Book");
    }

    @Test
    void findAll_ReturnsAllPersistedStats() {
        List<Stats> all = statsRepository.findAll();

        assertThat(all).hasSize(4);
    }

    @Test
    void delete_ExistingStats_RemovesFromDatabase() {
        Stats toDelete = statsRepository.findByBookTitle("Donnie Darko").get(0);
        statsRepository.delete(toDelete);

        assertThat(statsRepository.findByBookTitle("Donnie Darko")).isEmpty();
    }
}
