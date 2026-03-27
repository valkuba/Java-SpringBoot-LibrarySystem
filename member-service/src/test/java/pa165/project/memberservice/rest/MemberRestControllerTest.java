package pa165.project.memberservice.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.facade.MemberFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberRestControllerTest {

    @Mock
    private MemberFacade memberFacade;

    @InjectMocks
    private MemberRestController memberRestController;

    private MemberDto testMemberDto;
    private NewMemberRegisteringDto testNewMemberDto;

    @BeforeEach
    void setUp() {
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
    void getAllMembers_shouldReturnList() {
        when(memberFacade.getAllMembers()).thenReturn(List.of(testMemberDto));

        ResponseEntity<List<MemberDto>> response = memberRestController.getAllMembers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(memberFacade).getAllMembers();
    }

    @Test
    void getMemberById_shouldReturnMember() {
        when(memberFacade.getMemberById(1L)).thenReturn(Optional.of(testMemberDto));

        ResponseEntity<MemberDto> response = memberRestController.getMemberById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("john", response.getBody().getUsername());
        verify(memberFacade).getMemberById(1L);
    }

    @Test
    void getMemberById_notFound_shouldReturn404() {
        when(memberFacade.getMemberById(99L)).thenReturn(Optional.empty());

        ResponseEntity<MemberDto> response = memberRestController.getMemberById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(memberFacade).getMemberById(99L);
    }

    @Test
    void getMemberByEmail_shouldReturnMember() {
        when(memberFacade.getMemberWithEmail("john@test.com")).thenReturn(Optional.of(testMemberDto));

        ResponseEntity<MemberDto> response = memberRestController.getMemberByEmail("john@test.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("john", response.getBody().getUsername());
        verify(memberFacade).getMemberWithEmail("john@test.com");
    }

    @Test
    void getMemberByEmail_notFound_shouldReturn404() {
        when(memberFacade.getMemberWithEmail("unknown@test.com")).thenReturn(Optional.empty());

        ResponseEntity<MemberDto> response = memberRestController.getMemberByEmail("unknown@test.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(memberFacade).getMemberWithEmail("unknown@test.com");
    }

    @Test
    void getMemberByUsername_shouldReturnMember() {
        when(memberFacade.getMemberWithUsername("john")).thenReturn(Optional.of(testMemberDto));

        ResponseEntity<MemberDto> response = memberRestController.getMemberByUsername("john");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("john@test.com", response.getBody().getEmail());
        verify(memberFacade).getMemberWithUsername("john");
    }

    @Test
    void getMemberByUsername_notFound_shouldReturn404() {
        when(memberFacade.getMemberWithUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<MemberDto> response = memberRestController.getMemberByUsername("unknown");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(memberFacade).getMemberWithUsername("unknown");
    }

    @Test
    void registerMember_shouldReturnCreatedMember() {
        when(memberFacade.registerMember(testNewMemberDto)).thenReturn(testMemberDto);

        MemberDto response = memberRestController.registerMember(testNewMemberDto);

        assertNotNull(response);
        assertEquals("john", response.getUsername());
        verify(memberFacade).registerMember(testNewMemberDto);
    }

    @Test
    void activateMember_shouldCallFacade() {
        doNothing().when(memberFacade).activateMember(1L);

        memberRestController.activateMember(1L);

        verify(memberFacade).activateMember(1L);
    }

    @Test
    void updateMember_shouldReturnUpdatedMember() {
        when(memberFacade.updateMember(testMemberDto)).thenReturn(testMemberDto);

        MemberDto response = memberRestController.updateMember(1L, testMemberDto);

        assertNotNull(response);
        assertEquals("john", response.getUsername());
        verify(memberFacade).updateMember(testMemberDto);
    }

    @Test
    void removeMember_shouldCallFacade() {
        doNothing().when(memberFacade).removeMember(1L);

        memberRestController.removeMember(1L);

        verify(memberFacade).removeMember(1L);
    }
}