package pa165.project.notificationstatsservice.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.notificationstatsservice.config.SecurityConfig;
import pa165.project.notificationstatsservice.dto.PopularBookDto;
import pa165.project.notificationstatsservice.dto.StatsDto;
import pa165.project.notificationstatsservice.facade.StatsFacade;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@WebMvcTest(StatsRestController.class)
@Import(SecurityConfig.class)
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

        mockMvc.perform(get("/api/stats")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Blood of elves"))
                .andExpect(jsonPath("$[1].bookTitle").value("Last wish"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_test_read")
    void getTopNBooks_withDefaultLimit_shouldReturn200() throws Exception {
        when(statsFacade.getTopNBooks(5)).thenReturn(List.of());

        mockMvc.perform(get("/api/stats/popular-books")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_test_read")
    void getTopNBooks_withCustomLimit_shouldPassLimit() throws Exception {
        PopularBookDto dto = PopularBookDto.builder()
                .bookTitle("Blood of elves").borrowCount(10L).build();

        when(statsFacade.getTopNBooks(3)).thenReturn(List.of(dto)); // mock the facade to return the list of 3 popular books
        
        mockMvc.perform(get("/api/stats/popular-books").param("limit", "3")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Blood of elves"))
                .andExpect(jsonPath("$[0].borrowCount").value(10));
    }
}