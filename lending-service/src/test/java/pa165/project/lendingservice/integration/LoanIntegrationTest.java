package pa165.project.lendingservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.messaging.LendingMessageProducer;
import tools.jackson.databind.json.JsonMapper;
import pa165.project.lendingservice.dto.LoanDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LendingMessageProducer loanMessageProducer;

    private final JsonMapper jsonMapper = new JsonMapper();

    @Test
    public void createAndGetLoan_fullStack_returnsCreatedLoan() throws Exception {
        var loan = LoanDto.builder()
                .memberId(1L)
                .bookCopyId(1L)
                .loanDate(LocalDate.of(2026, 1, 1))
                .dueDate(LocalDate.of(2026, 1, 15))
                .status("LOANED")
                .build();

        // CREATE
        var responseJson = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, LoanDto.class);

        // GET
        mockMvc.perform(get("/api/loans/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.status").value("LOANED"));
    }
}