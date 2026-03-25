package pa165.project.memberservice.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pa165.project.memberservice.dto.MemberDto;
import pa165.project.memberservice.dto.NewMemberRegisteringDto;
import pa165.project.memberservice.mapper.MemberMapper;
import pa165.project.memberservice.service.MemberService;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    @Override
    public MemberDto registerMember(NewMemberRegisteringDto newMemberRegisteringDto) {
        var model = memberService.registerMember(memberMapper.toModel(newMemberRegisteringDto));
        return memberMapper.toDto(model);
    }

    @Override
    public void activateMember(Long id) {
        memberService.activateMember(id);
    }

    @Override
    public List<MemberDto> getAllMembers() {
        return memberService.getAllMembers().stream().map(memberMapper::toDto).toList();
    }

    @Override
    public Optional<MemberDto> getMemberById(Long id) {
        return memberService.getMemberById(id).map(memberMapper::toDto);
    }

    @Override
    public MemberDto updateMember(MemberDto memberDto) {
        var model = memberMapper.toModel(memberDto);
        var updatedModel = memberService.updateMember(model);
        return memberMapper.toDto(updatedModel);
    }

    @Override
    public void removeMember(Long id) {
        memberService.removeMember(id);
    }

    @Override
    public Optional<MemberDto> getMemberWithEmail(String email) {
        return memberService.getMemberWithEmail(email).map(memberMapper::toDto);
    }

    @Override
    public Optional<MemberDto> getMemberWithUsername(String username) {
        return memberService.getMemberWithUsername(username).map(memberMapper::toDto);
    }
}
