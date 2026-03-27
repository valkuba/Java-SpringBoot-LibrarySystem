package pa165.project.memberservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.MemberUpdateException;
import pa165.project.memberservice.repository.MemberRepository;
import pa165.project.memberservice.repository.MemberRepositoryException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member createTestMember() {
        return Member.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .password("password")
                .registrationDate(new Date())
                .activated(false)
                .build();
    }

    @Test
    void registerMember_shouldRegisterMember() throws Exception {
        Member member = createTestMember();

        doNothing().when(memberRepository).register(member);

        Member result = memberService.registerMember(member);

        assertNotNull(result);
        assertEquals(member.getUsername(), result.getUsername());
        verify(memberRepository).register(member);
    }

    @Test
    void registerInvalidMember_shouldThrow() throws Exception {
        Member invalidMember = Member.builder()
                .id(1L)
                .username(null)       // missing name
                .email("test@example.com")
                .password("secret")
                .registrationDate(new Date())
                .build();

        doThrow(new MemberRepositoryException("Username is required"))
                .when(memberRepository)
                .register(invalidMember);

        assertThrows(MemberRepositoryException.class, () -> {
            memberService.registerMember(invalidMember);
        });

        verify(memberRepository).register(invalidMember);
    }

    @Test
    void activateMember_shouldCallRepository() throws Exception {
        Long id = 1L;

        doNothing().when(memberRepository).activateUser(id);

        memberService.activateMember(id);

        verify(memberRepository).activateUser(id);
    }

    @Test
    void activateMemberInvalidId_shouldCallRepository_shouldThrow() throws Exception {
        Long invalidId = null;

        doThrow(new MemberRepositoryException("Id is required"))
                .when(memberRepository)
                .activateUser(invalidId);

        assertThrows(MemberRepositoryException.class, () -> {
            memberService.activateMember(invalidId);
        });

        verify(memberRepository).activateUser(invalidId);
    }

    @Test
    void getAllMembers_shouldReturnMembers() {
        Member member = createTestMember();
        when(memberRepository.findAll()).thenReturn(List.of(member));

        List<Member> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        verify(memberRepository).findAll();
    }

    @Test
    void getMemberById_shouldReturnMember() {
        Member member = createTestMember();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Optional<Member> result = memberService.getMemberById(1L);

        assertTrue(result.isPresent());
        assertEquals(member.getUsername(), result.get().getUsername());
    }

    @Test
    void getMemberById_notFound_shouldReturnEmpty() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Member> result = memberService.getMemberById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateMember_shouldUpdateMember() throws Exception {
        Member member = createTestMember();

        when(memberRepository.update(member)).thenReturn(member);

        Member result = memberService.updateMember(member);

        assertNotNull(result);
        verify(memberRepository).update(member);
    }

    @Test
    void updateMember_invalidChange_shouldThrowMemberUpdateException() throws Exception {
        Member memberToUpdate = createTestMember();
        memberToUpdate.setId(2L);

        doThrow(new MemberUpdateException("Cannot change member's id"))
                .when(memberRepository).update(memberToUpdate);

        assertThrows(MemberUpdateException.class, () -> memberService.updateMember(memberToUpdate));
        verify(memberRepository).update(memberToUpdate);
    }

    @Test
    void removeMember_shouldCallRepositoryDelete() {
        Long id = 1L;

        doNothing().when(memberRepository).deleteById(id);

        memberService.removeMember(id);

        verify(memberRepository).deleteById(id);
    }

    @Test
    void getMemberWithEmail_shouldReturnMember() {
        Member member = createTestMember();

        when(memberRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(member));

        Optional<Member> result = memberService.getMemberWithEmail("john@test.com");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void getMemberWithUsername_shouldReturnMember() {
        Member member = createTestMember();

        when(memberRepository.findByUsername("john"))
                .thenReturn(Optional.of(member));

        Optional<Member> result = memberService.getMemberWithUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john@test.com", result.get().getEmail());
    }
}