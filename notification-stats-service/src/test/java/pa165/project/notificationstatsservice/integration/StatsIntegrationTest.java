package pa165.project.notificationstatsservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.notificationstatsservice.messaging.NotificationMessageConsumer;
import pa165.project.notificationstatsservice.model.Stats;
import pa165.project.notificationstatsservice.repository.StatsRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatsRepository statsRepository;

    @MockitoBean
    private NotificationMessageConsumer notificationMessageConsumer;

    @BeforeEach
    void setup() {
        statsRepository.deleteAll();
    }

    @Test
    void getAllStats_fullStack_returnsPersistedStats() throws Exception {
        statsRepository.save(Stats.builder().bookId(1L).bookTitle("Blood of elves").returned(true).build());
        statsRepository.save(Stats.builder().bookId(2L).bookTitle("Last wish").returned(false).build());

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTopNBooks_defaultLimit_returnsTopFiveBooks() throws Exception {
        seedStats("The Great Gatsby", 6, 1L);
        seedStats("Fahrenheit 451", 5, 13L);
        seedStats("How to get rich", 4, 33L);
        seedStats("Sleep 101", 3, 67L);
        seedStats("Best selling book", 2, 99L);
        seedStats("Random book title", 1, 101L);

        mockMvc.perform(get("/api/stats/popular-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].bookTitle").value("The Great Gatsby"))
                .andExpect(jsonPath("$[0].borrowCount").value(6))
                .andExpect(jsonPath("$[1].bookTitle").value("Fahrenheit 451"))
                .andExpect(jsonPath("$[1].borrowCount").value(5))
                .andExpect(jsonPath("$[2].bookTitle").value("How to get rich"))
                .andExpect(jsonPath("$[2].borrowCount").value(4))
                .andExpect(jsonPath("$[3].bookTitle").value("Sleep 101"))
                .andExpect(jsonPath("$[3].borrowCount").value(3))
                .andExpect(jsonPath("$[4].bookTitle").value("Best selling book"))
                .andExpect(jsonPath("$[4].borrowCount").value(2));
    }

    @Test
    void getTopNBooks_customLimit_returnsRequestedNumberOfBooks() throws Exception {
        seedStats("The Great Gatsby", 4, 1L);
        seedStats("Fahrenheit 451", 3, 13L);
        seedStats("How to get rich", 2, 33L);

        mockMvc.perform(get("/api/stats/popular-books").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bookTitle").value("The Great Gatsby"))
                .andExpect(jsonPath("$[0].borrowCount").value(4))
                .andExpect(jsonPath("$[1].bookTitle").value("Fahrenheit 451"))
                .andExpect(jsonPath("$[1].borrowCount").value(3));
    }

    private void seedStats(String bookTitle, int count, long baseBookId) {
        for (int i = 0; i < count; i++) {
            statsRepository.save(
                    Stats.builder()
                            .bookId(baseBookId + i)
                            .bookTitle(bookTitle)
                            .returned(true)
                            .build()
            );
        }
    }
}
