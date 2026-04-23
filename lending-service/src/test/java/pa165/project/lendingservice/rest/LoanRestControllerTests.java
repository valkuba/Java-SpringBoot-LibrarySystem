package pa165.project.lendingservice.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.dto.LoanDto;
import pa165.project.lendingservice.facade.LoanFacade;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanFacade loanFacade;

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Test
    public void getAllLends_loansExist_returnsOkWithList() throws Exception {
        Mockito.when(loanFacade.getAllLoans()).thenReturn(List.of(
                LoanDto.builder().id(1L).build(),
                LoanDto.builder().id(2L).build(),
                LoanDto.builder().id(3L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/loans")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, LoanDto[].class);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(LoanDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getLendById_existingId_returnsOk()throws Exception {
        Mockito.when(loanFacade.getLoanById(1L)).thenReturn(Optional.of(LoanDto.builder().id(1L).build()));

        var responseJson = mockMvc.perform(get("/api/loans/{id}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, LoanDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void getLendById_nonExistingId_returnsNotFound() throws Exception {
        Mockito.when(loanFacade.getLoanById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/loans/{id}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createLend_validDto_returnsCreated() throws Exception {
        Mockito.when(loanFacade.createLoan(Mockito.any())).thenReturn(LoanDto.builder().id(1L).memberId(1L).bookCopyId(1L).status("UNPAID").build());

        var responseJson = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(LoanDto.builder().id(1L).memberId(1L).bookCopyId(1L).status("UNPAID").build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, LoanDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void updateLend_existingId_returnsOk() throws Exception {
        Mockito.when(loanFacade.updateLoan(Mockito.eq(1L),Mockito.any())).thenReturn(LoanDto.builder().id(1L).memberId(1L).bookCopyId(1L).status("LOANED").build());

        var responseJson = mockMvc.perform(put("/api/loans/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(LoanDto.builder().id(1L).memberId(1L).bookCopyId(1L).status("LOANED").build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, LoanDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void returnLoan_existingId_returnsOk() throws Exception {
        var returnDate = LocalDate.of(2026, 2, 1);
        Mockito.when(loanFacade.returnLoan(eq(1L), eq(returnDate)))
                .thenReturn(LoanDto.builder().id(1L).returnDate(returnDate).status("RETURNED").build());

        var responseJson = mockMvc.perform(put("/api/loans/{id}/return", 1L)
                        .param("returnDate", returnDate.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, LoanDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo("RETURNED");
    }

    @Test
    public void deleteLend_existingId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/loans/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(loanFacade, Mockito.times(1)).deleteLoan(1L);
    }

    @Test
    public void searchLends_withFilters_returnsOkWithFilteredList()throws Exception {
            Mockito.when(loanFacade.searchLoan(1L,2L)).thenReturn(List.of(
                    LoanDto.builder().id(1L).memberId(1L).bookCopyId(2L).build(),
                    LoanDto.builder().id(3L).memberId(1L).bookCopyId(2L).build()
            ));

            var responseJson = mockMvc.perform(get("/api/loans/search")
                            .param("memberId", "1")
                            .param("bookCopyId", "2")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            var response = jsonMapper.readValue(responseJson, LoanDto[].class);

            assertThat(response).hasSize(2);
            assertThat(response).extracting(LoanDto::getId).containsExactlyInAnyOrder(1L, 3L);
    }
}
