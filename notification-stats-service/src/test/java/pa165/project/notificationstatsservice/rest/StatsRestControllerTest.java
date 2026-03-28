package pa165.project.notificationstatsservice.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.facade.StatsFacade;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;


@WebMvcTest(StatsRestController.class)
class StatsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatsFacade statsFacade;

    @Test
    void getAllStats() throws Exception {
        List<StatsDto> dtoList = List.of(
            StatsDto.builder().id(1L).bookTitle("Blood of elves").returned(true).build(),
            StatsDto.builder().id(2L).bookTitle("Last wish").returned(true).build()
        );

        when(statsFacade.getAllStats()).thenReturn(dtoList);

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Blood of elves"))
                .andExpect(jsonPath("$[1].bookTitle").value("Last wish"));
    }

    @Test
    void getTopNBooks_withDefaultLimit_shouldReturn200() throws Exception {
        when(statsFacade.getTopNBooks(5)).thenReturn(List.of());

        mockMvc.perform(get("/api/stats/popular-books"))
                .andExpect(status().isOk());
    }

    @Test
    void getTopNBooks_withCustomLimit_shouldPassLimit() throws Exception {
        PopularBookDto dto = PopularBookDto.builder()
                .bookTitle("Blood of elves").borrowCount(10L).build();

        when(statsFacade.getTopNBooks(3)).thenReturn(List.of(dto)); // mock the facade to return the list of 3 popular books
        
        mockMvc.perform(get("/api/stats/popular-books").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Blood of elves"))
                .andExpect(jsonPath("$[0].borrowCount").value(10));
    }
}