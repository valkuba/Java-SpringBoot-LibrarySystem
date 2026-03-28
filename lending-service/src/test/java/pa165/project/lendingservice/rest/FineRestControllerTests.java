package pa165.project.lendingservice.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.lendingservice.dto.FineDto;
import pa165.project.lendingservice.facade.FineFacadeImpl;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class FineRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FineFacadeImpl personFacade;

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Test
    public void getAllFines_finesExist_returnsOkWithList() throws Exception {
        Mockito.when(personFacade.getAllFines()).thenReturn(List.of(
                FineDto.builder().id(1L).build(),
                FineDto.builder().id(2L).build(),
                FineDto.builder().id(3L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/fines")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, FineDto[].class);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(FineDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getFineById_existingId_returnsOk() throws Exception {
        Mockito.when(personFacade.getFineById(1L)).thenReturn(Optional.of(FineDto.builder().id(1L).build()));

        var responseJson = mockMvc.perform(get("/api/fines/{id}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, FineDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void getFineById_nonExistingId_returnsNotFound() throws Exception {
            Mockito.when(personFacade.getFineById(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/fines/{id}",1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void getFinesByMember_memberWithFines_returnsOkWithList() throws Exception {
        Mockito.when(personFacade.getFinesByMemberId(1L)).thenReturn(List.of(
                FineDto.builder().id(1L).build(),
                FineDto.builder().id(3L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/fines/member/{memberId}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, FineDto[].class);

        assertThat(response).hasSize(2);
        assertThat(response).extracting(FineDto::getId).containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    public void createFine_validDto_returnsCreated() throws Exception {
        Mockito.when(personFacade.createFine(Mockito.any())).thenReturn(FineDto.builder().id(1L).build());

        var responseJson = mockMvc.perform(post("/api/fines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(FineDto.builder().id(1L).build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, FineDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void updateFine_existingId_returnsOk() throws Exception {
        Mockito.when(personFacade.updateFine(Mockito.any())).thenReturn(FineDto.builder().id(1L).build());

        var responseJson = mockMvc.perform(put("/api/fines/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(FineDto.builder().id(1L).build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, FineDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void deleteFine_existingId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/fines/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(personFacade, Mockito.times(1)).deleteFine(1L);
    }
}
