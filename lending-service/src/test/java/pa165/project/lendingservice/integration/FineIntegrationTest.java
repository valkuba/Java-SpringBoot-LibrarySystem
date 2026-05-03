package pa165.project.lendingservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.messaging.LendingMessageProducer;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LendingMessageProducer lendingMessageProducer;

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
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(fine)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, FineDto.class);

        // GET
        mockMvc.perform(get("/api/fines/{id}", created.getId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(10L))
                .andExpect(jsonPath("$.status").value("UNPAID"));
    }
}