package pa165.project.lendingservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.dto.FineDto;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper = new JsonMapper();

    @Test
    public void createAndGetLoan_fullStack_returnsCreatedLoan() throws Exception {
        var fine = FineDto.builder()
                .memberId(10L)
                .loanId(100L)
                .date(LocalDate.of(2026, 1, 15))
                .amount(15.0)
                .status("UNPAID")
                .build();

        // CREATE
        var responseJson = mockMvc.perform(post("/api/fines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(fine)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, FineDto.class);

        // GET
        mockMvc.perform(get("/api/fines/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(10L))
                .andExpect(jsonPath("$.status").value("UNPAID"));
    }
}