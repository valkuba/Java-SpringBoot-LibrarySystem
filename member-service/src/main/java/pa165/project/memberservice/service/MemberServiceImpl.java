package pa165.project.memberservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.MemberUpdateException;
import pa165.project.memberservice.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public Member registerMember(Member member) throws MemberRepositoryException {
        memberRepository.register(member);
        return member;
    }

    @Override
    public void activateMember(Long id) throws MemberRepositoryException {
        memberRepository.activateUser(id);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member updateMember(Member member) throws MemberRepositoryException, MemberUpdateException {
        return memberRepository.update(member);
    }

    @Override
    public void removeMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Optional<Member> getMemberWithEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> getMemberWithUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
