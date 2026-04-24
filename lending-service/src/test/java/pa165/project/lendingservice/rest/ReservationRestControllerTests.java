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
import pa165.project.lendingservice.dto.ReservationDto;
import pa165.project.lendingservice.facade.ReservationFacade;
import tools.jackson.databind.DeserializationFeature;
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
public class ReservationRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationFacade reservationFacade;

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Test
    public void getAllReservations_reservationsExist_returnsOkWithList() throws Exception {
        Mockito.when(reservationFacade.getAllReservations()).thenReturn(List.of(
                ReservationDto.builder().id(1L).build(),
                ReservationDto.builder().id(2L).build(),
                ReservationDto.builder().id(3L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/reservations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, ReservationDto[].class);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(ReservationDto::getId).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    public void getReservationById_existingId_returnsOk() throws  Exception {
        Mockito.when(reservationFacade.getReservationById(1L)).thenReturn(Optional.of(ReservationDto.builder().id(1L).build()));

        var responseJson = mockMvc.perform(get("/api/reservations/{id}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, ReservationDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void getReservationById_nonExistingId_returnsNotFound() throws Exception {
        Mockito.when(reservationFacade.getReservationById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/{id}",1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getReservationsByMember_memberWithReservations_returnsOkWithList() throws Exception {
        Mockito.when(reservationFacade.getReservationsByMemberId(1L)).thenReturn(List.of(
                ReservationDto.builder().id(1L).memberId(1L).build(),
                ReservationDto.builder().id(2L).memberId(1L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/reservations/member/{memberId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, ReservationDto[].class);

        assertThat(response).hasSize(2);
        assertThat(response).extracting(ReservationDto::getMemberId)
                .containsOnly(1L);
    }

    @Test
    public void getReservationsByBook_bookWithReservations_returnsOkWithList() throws Exception {
        Mockito.when(reservationFacade.getReservationsByBookId(10L)).thenReturn(List.of(
                ReservationDto.builder().id(1L).bookId(10L).build(),
                ReservationDto.builder().id(2L).bookId(10L).build(),
                ReservationDto.builder().id(3L).bookId(10L).build()
        ));

        var responseJson = mockMvc.perform(get("/api/reservations/book/{bookId}", 10L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        var response = jsonMapper.readValue(responseJson, ReservationDto[].class);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(ReservationDto::getBookId)
                .containsOnly(10L);
    }

    @Test
    public void createReservation_validDto_returnsCreated() throws Exception {
        Mockito.when(reservationFacade.createReservation(Mockito.any())).thenReturn(ReservationDto.builder().id(1L).memberId(1L).status("UNPAID").build());

        var responseJson = mockMvc.perform(post("/api/reservations")
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
    public void updateReservation_existingId_returnsOk() throws Exception {
        Mockito.when(reservationFacade.updateReservation(Mockito.any())).thenReturn(ReservationDto.builder().id(1L).memberId(1L).status("LOANED").build());

        var responseJson = mockMvc.perform(put("/api/reservations/{id}", 1L)
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
    public void deleteReservation_existingId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/reservations/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(reservationFacade, Mockito.times(1)).deleteReservation(1L);
    }
}
