package pa165.project.memberservice.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.mapper.MemberMapper;
import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.exception.MemberUpdateException;
import pa165.project.memberservice.service.MemberService;
import pa165.project.memberservice.service.exception.MemberServiceException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberFacadeImplTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberFacadeImpl memberFacade;

    private Member testMemberModel;
    private MemberDto testMemberDto;
    private NewMemberRegisteringDto testNewMemberDto;

    @BeforeEach
    void setUp() {
        testMemberModel = Member.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .build();

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
    void registerMember_shouldReturnDto() {
        when(memberMapper.toModel(testNewMemberDto)).thenReturn(testMemberModel);
        when(memberService.registerMember(testMemberModel)).thenReturn(testMemberModel);
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        MemberDto result = memberFacade.registerMember(testNewMemberDto);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(memberMapper).toModel(testNewMemberDto);
        verify(memberService).registerMember(testMemberModel);
        verify(memberMapper).toDto(testMemberModel);
    }

    @Test
    void activateMember_shouldCallService() {
        doNothing().when(memberService).activateMember(1L);

        memberFacade.activateMember(1L);

        verify(memberService).activateMember(1L);
    }

    @Test
    void getAllMembers_shouldReturnDtoList() {
        when(memberService.getAllMembers()).thenReturn(List.of(testMemberModel));
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        List<MemberDto> result = memberFacade.getAllMembers();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
        verify(memberService).getAllMembers();
        verify(memberMapper).toDto(testMemberModel);
    }

    @Test
    void getMemberById_shouldReturnOptionalDto() {
        when(memberService.getMemberById(1L)).thenReturn(Optional.of(testMemberModel));
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        Optional<MemberDto> result = memberFacade.getMemberById(1L);

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(memberService).getMemberById(1L);
        verify(memberMapper).toDto(testMemberModel);
    }

    @Test
    void updateMember_shouldReturnUpdatedDto() throws MemberServiceException, MemberUpdateException {
        when(memberMapper.toModel(testMemberDto)).thenReturn(testMemberModel);
        when(memberService.updateMember(testMemberModel)).thenReturn(testMemberModel);
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        MemberDto result = memberFacade.updateMember(testMemberDto);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(memberMapper).toModel(testMemberDto);
        verify(memberService).updateMember(testMemberModel);
        verify(memberMapper).toDto(testMemberModel);
    }

    @Test
    void removeMember_shouldCallService() {
        doNothing().when(memberService).removeMember(1L);

        memberFacade.removeMember(1L);

        verify(memberService).removeMember(1L);
    }

    @Test
    void getMemberWithEmail_shouldReturnOptionalDto() {
        when(memberService.getMemberWithEmail("john@test.com")).thenReturn(Optional.of(testMemberModel));
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        Optional<MemberDto> result = memberFacade.getMemberWithEmail("john@test.com");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(memberService).getMemberWithEmail("john@test.com");
        verify(memberMapper).toDto(testMemberModel);
    }

    @Test
    void getMemberWithUsername_shouldReturnOptionalDto() {
        when(memberService.getMemberWithUsername("john")).thenReturn(Optional.of(testMemberModel));
        when(memberMapper.toDto(testMemberModel)).thenReturn(testMemberDto);

        Optional<MemberDto> result = memberFacade.getMemberWithUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john@test.com", result.get().getEmail());
        verify(memberService).getMemberWithUsername("john");
        verify(memberMapper).toDto(testMemberModel);
    }
}