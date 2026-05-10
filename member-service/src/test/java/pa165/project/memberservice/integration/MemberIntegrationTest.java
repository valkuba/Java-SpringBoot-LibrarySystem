package pa165.project.memberservice.integration;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import pa165.project.memberservice.config.TestMetricsConfig;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.security.SecurityConfig;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestMetricsConfig.class)
public class MemberIntegrationTest {

    private static final RequestPostProcessor AUTH = jwt().authorities(
            new SimpleGrantedAuthority(SecurityConfig.AUTHORITY_VISITOR),
            new SimpleGrantedAuthority(SecurityConfig.AUTHORITY_MEMBER),
            new SimpleGrantedAuthority(SecurityConfig.AUTHORITY_ADMIN)
    );

    @MockitoBean
    JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    private static NewMemberRegisteringDto getJaneRegisteringDto() {
        return NewMemberRegisteringDto.builder()
                .username("jane")
                .email("jane@test.com")
                .password("password")
                .build();
    }

    private static NewMemberRegisteringDto getJohnRegisteringDto() {
        return NewMemberRegisteringDto.builder()
                .username("john")
                .email("john@test.com")
                .password("password")
                .build();
    }

    private MemberDto ensureMemberRegistered(NewMemberRegisteringDto memberDto) throws Exception {
        String response = mockMvc.perform(post("/api/members/register")
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(memberDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(memberDto.getUsername()))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        MemberDto result = jsonMapper.readValue(response, MemberDto.class);
        assertNotNull(result);
        assertNotNull(result.getId());
        return result;
    }

    @Test
    void registerMember_shouldReturnCreated() throws Exception {
        ensureMemberRegistered(getJaneRegisteringDto());
    }

    @Test
    void registerMember_duplicateUsername_shouldFail() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        ensureMemberRegistered(janeRegisteringDto);

        var conflictUsernameMember = getJohnRegisteringDto();
        conflictUsernameMember.setUsername(janeRegisteringDto.getUsername());

        mockMvc.perform(post("/api/members/register")
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(conflictUsernameMember)))
                .andExpect(status().isConflict());
    }

    @Test
    void registerMember_duplicateEmail_shouldFail() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        ensureMemberRegistered(janeRegisteringDto);

        var conflictUsernameMember = getJohnRegisteringDto();
        conflictUsernameMember.setEmail(janeRegisteringDto.getEmail());

        mockMvc.perform(post("/api/members/register")
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(conflictUsernameMember)))
                .andExpect(status().isConflict());
    }

    @Test
    void registerMember_invalidInput_shouldReturn400() throws Exception {
        var invalidMember = NewMemberRegisteringDto.builder().email("").username("").build();
        mockMvc.perform(post("/api/members/register")
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidMember)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllMembers_shouldReturnList() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        ensureMemberRegistered(janeRegisteringDto);

        mockMvc.perform(get("/api/members").with(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value(janeRegisteringDto.getUsername()))
                .andExpect(jsonPath("$[0].email").value(janeRegisteringDto.getEmail()));
    }

    @Test
    void getMemberById_existingId_shouldReturnMember() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        long id = ensureMemberRegistered(janeRegisteringDto).getId();

        mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(janeRegisteringDto.getUsername()))
                .andExpect(jsonPath("$.email").value(janeRegisteringDto.getEmail()));
    }

    @Test
    void getMemberById_nonExistingId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/members/99999").with(AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMemberByEmail_existingEmail_shouldReturnMember() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        ensureMemberRegistered(janeRegisteringDto);

        mockMvc.perform(get("/api/members/by-email")
                        .with(AUTH)
                        .param("email", janeRegisteringDto.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(janeRegisteringDto.getUsername()))
                .andExpect(jsonPath("$.email").value(janeRegisteringDto.getEmail()));
    }

    @Test
    void getMemberByEmail_nonExistingEmail_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/members/by-email")
                        .with(AUTH)
                        .param("email", "nonexistent@test.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMemberByUsername_existingUsername_shouldReturnMember() throws Exception {
        var janeRegisteringDto = getJaneRegisteringDto();
        ensureMemberRegistered(janeRegisteringDto);

        mockMvc.perform(get("/api/members/by-username")
                        .with(AUTH)
                        .param("username", janeRegisteringDto.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(janeRegisteringDto.getUsername()))
                .andExpect(jsonPath("$.email").value(janeRegisteringDto.getEmail()));
    }

    @Test
    void getMemberByUsername_nonExistingUsername_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/members/by-username")
                        .with(AUTH)
                        .param("username", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void activateMember_shouldSetActivatedTrue() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        mockMvc.perform(patch("/api/members/activate/" + id).with(AUTH))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activated").value(true));
    }

    @Test
    void activateMember_alreadyActivated_shouldFail() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        mockMvc.perform(patch("/api/members/activate/" + id).with(AUTH))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/api/members/activate/" + id).with(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activateMember_nonExistingId_shouldFail() throws Exception {
        mockMvc.perform(patch("/api/members/activate/99999").with(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMember_shouldReturnUpdatedMember() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        String getResponse = mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberDto memberDto = jsonMapper.readValue(getResponse, MemberDto.class);
        memberDto.setUsername("jane_updated");

        mockMvc.perform(put("/api/members/update/" + id)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jane_updated"))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()));
    }

    @Test
    void updateMember_nonExistingMember_shouldFail() throws Exception {
        MemberDto nonExistingMember = MemberDto.builder()
                .id(99999L)
                .username("ghost")
                .email("ghost@test.com")
                .build();

        mockMvc.perform(put("/api/members/update/99999")
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(nonExistingMember)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMember_changeRegistrationDate_shouldFail() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        String getResponse = mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberDto memberDto = jsonMapper.readValue(getResponse, MemberDto.class);
        memberDto.setRegistrationDate(new Date(0));

        mockMvc.perform(put("/api/members/update/" + id)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMember_changeActivationStatus_shouldFail() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        String getResponse = mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MemberDto memberDto = jsonMapper.readValue(getResponse, MemberDto.class);
        memberDto.setActivated(true);

        mockMvc.perform(put("/api/members/update/" + id)
                        .with(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(memberDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeMember_shouldReturn204AndMemberNotFound() throws Exception {
        long id = ensureMemberRegistered(getJaneRegisteringDto()).getId();

        mockMvc.perform(delete("/api/members/remove/" + id).with(AUTH))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/members/" + id).with(AUTH))
                .andExpect(status().isNotFound());
    }
}
