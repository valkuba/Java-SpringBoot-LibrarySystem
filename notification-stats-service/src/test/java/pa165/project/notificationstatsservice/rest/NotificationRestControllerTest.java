package pa165.project.notificationstatsservice.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.notificationstatsservice.config.SecurityConfig;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.facade.NotificationFacade;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationRestController.class)
@Import(SecurityConfig.class)
class NotificationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationFacade notificationFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JmsTemplate jmsTemplate;

    @Test
    void getAllNotifications_shouldReturn200() throws Exception {
        when(notificationFacade.getAllNotifications()).thenReturn(List.of());

        mockMvc.perform(get("/api/notifications")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk());
    }

    @Test
    void getById_whenNotFound_shouldReturn404() throws Exception {
        when(notificationFacade.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/99")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_whenFound_shouldReturn200() throws Exception {
        NotificationDto dto = NotificationDto.builder().id(1L).message("Test").build();

        when(notificationFacade.getById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/notifications/1")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createNotification_withBlankMessage_shouldReturn400() throws Exception {
        CreateNotificationDto dto = CreateNotificationDto.builder()
                .message("")
                .recipientEmail("test@gmail.com")
                .build();

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotification_withInvalidEmail_shouldReturn400() throws Exception {
        CreateNotificationDto dto = CreateNotificationDto.builder()
                .message("Test")
                .recipientEmail("testgmail.com")
                .build();

        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteNotification() throws Exception {
        mockMvc.perform(delete("/api/notifications/1")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void createOverdueNotification_shouldReturn201() throws Exception {
        NotificationDto dto = NotificationDto.builder().id(1L)
                .message("overdue")
                .recipientEmail("test@gmail.com")
                .build();

        when(notificationFacade.createOverdueNotification("test@gmail.com", "Blood of elves"))
                .thenReturn(dto);

        mockMvc.perform(post("/api/notifications/overdue")
                .param("recipientEmail", "test@gmail.com")
                .param("bookTitle", "Blood of elves")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void getAllSortedByDate() throws Exception {
        List<NotificationDto> dtoList = List.of(
                NotificationDto.builder().id(2L).message("overdue 2").recipientEmail("test@gmail.com").build(),  // newer first
                NotificationDto.builder().id(1L).message("overdue").recipientEmail("test@gmail.com").build()
        );

        when(notificationFacade.getAllSortedByDate()).thenReturn(dtoList);

        mockMvc.perform(get("/api/notifications/sorted")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[1].id").value(1L));
    }
}