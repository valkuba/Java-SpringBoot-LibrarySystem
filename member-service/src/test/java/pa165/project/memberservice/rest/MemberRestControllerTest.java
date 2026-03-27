package pa165.project.memberservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.facade.MemberFacade;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberRestControllerTest {

    @Mock
    private MemberFacade memberFacade;

    @InjectMocks
    private MemberRestController memberRestController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private MemberDto testMemberDto;
    private NewMemberRegisteringDto testNewMemberDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(memberRestController).build();

        testMemberDto = MemberDto.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .build();

        testNewMemberDto = NewMemberRegisteringDto.builder()
                .username("jane")
                .email("jane@test.com")
                .password("password")
                .build();
    }

    @Test
    void getAllMembers_shouldReturnList() throws Exception {
        when(memberFacade.getAllMembers()).thenReturn(List.of(testMemberDto));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("john"));

        verify(memberFacade).getAllMembers();
    }

    @Test
    void getMemberById_shouldReturnMember() throws Exception {
        when(memberFacade.getMemberById(1L)).thenReturn(Optional.of(testMemberDto));

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(memberFacade).getMemberById(1L);
    }

    @Test
    void getMemberById_notFound_shouldReturn404() throws Exception {
        when(memberFacade.getMemberById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/members/99"))
                .andExpect(status().isNotFound());

        verify(memberFacade).getMemberById(99L);
    }

    @Test
    void registerMember_shouldReturnCreatedMember() throws Exception {
        when(memberFacade.registerMember(testNewMemberDto)).thenReturn(testMemberDto);

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNewMemberDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"));

        verify(memberFacade).registerMember(testNewMemberDto);
    }

    @Test
    void activateMember_shouldCallFacade() throws Exception {
        doNothing().when(memberFacade).activateMember(1L);

        mockMvc.perform(patch("/api/members/activate/1"))
                .andExpect(status().isNoContent());

        verify(memberFacade).activateMember(1L);
    }

    @Test
    void updateMember_shouldReturnUpdatedMember() throws Exception {
        when(memberFacade.updateMember(testMemberDto)).thenReturn(testMemberDto);

        mockMvc.perform(put("/api/members/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMemberDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(memberFacade).updateMember(testMemberDto);
    }

    @Test
    void removeMember_shouldCallFacade() throws Exception {
        doNothing().when(memberFacade).removeMember(1L);

        mockMvc.perform(delete("/api/members/remove/1"))
                .andExpect(status().isNoContent());

        verify(memberFacade).removeMember(1L);
    }
}