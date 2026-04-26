package pa165.project.lendingservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.messaging.LendingMessageProducer;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LendingMessageProducer lendingMessageProducer;

    private final JsonMapper jsonMapper = new JsonMapper();

    @Test
    public void createAndGetLoan_fullStack_returnsCreatedLoan() throws Exception {
        var reservation = ReservationDto.builder()
                .memberId(10L)
                .bookId(100L)
                .reservationDate(LocalDate.of(2026, 1, 1))
                .whenAvailableDate(LocalDate.of(2026, 1, 10))
                .status("PENDING")
                .build();

        // CREATE
        var responseJson = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, ReservationDto.class);

        // GET
        mockMvc.perform(get("/api/reservations/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(10L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}