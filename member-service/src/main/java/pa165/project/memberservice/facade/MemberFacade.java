package pa165.project.memberservice.facade;

import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;

import java.util.List;
import java.util.Optional;

public interface MemberFacade {
    MemberDto registerMember(NewMemberRegisteringDto newMemberRegisteringDto);
    void activateMember(Long id);
    List<MemberDto> getAllMembers();
    Optional<MemberDto> getMemberById(Long id);
    MemberDto updateMember(MemberDto memberDto);
    void removeMember(Long id);
    Optional<MemberDto> getMemberWithEmail(String email);
    Optional<MemberDto> getMemberWithUsername(String username);
}
