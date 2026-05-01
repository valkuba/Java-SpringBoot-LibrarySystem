package pa165.project.notificationstatsservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pa165.project.notificationstatsservice.config.SecurityConfig;
import tools.jackson.databind.json.JsonMapper;
import pa165.project.notificationstatsservice.dto.CreateNotificationDto;
import pa165.project.notificationstatsservice.dto.NotificationDto;
import pa165.project.notificationstatsservice.messaging.NotificationMessageConsumer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationMessageConsumer notificationMessageConsumer;

    private final JsonMapper jsonMapper = new JsonMapper();

    @Test
    public void createAndGetNotification_fullStack_returnsCreatedNotification() throws Exception {
        var dto = CreateNotificationDto.builder()
                .message("Your book is overdue.")
                .recipientEmail("lospollos@hermanos.com")
                .build();

        var responseJson = mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dto))
                            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, NotificationDto.class);

        mockMvc.perform(get("/api/notifications/{id}", created.getId())
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your book is overdue."))
                .andExpect(jsonPath("$.recipientEmail").value("lospollos@hermanos.com"));
    }

    @Test
    public void getAllNotifications_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications")
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_read"))))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNotification_returnsNoContent() throws Exception {
        var dto = CreateNotificationDto.builder()
                .message("Very important message, I hope it wont get deleted.")
                .recipientEmail("noplease@dontdelete.com")
                .build();

        var responseJson = mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(dto))
                            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var created = jsonMapper.readValue(responseJson, NotificationDto.class);

        mockMvc.perform(delete("/api/notifications/{id}", created.getId())
                    .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_test_write"))))
                .andExpect(status().isNoContent());
    }
}