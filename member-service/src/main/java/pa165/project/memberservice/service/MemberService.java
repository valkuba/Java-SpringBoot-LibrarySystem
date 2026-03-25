package pa165.project.memberservice.service;

import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.MemberUpdateException;
import pa165.project.memberservice.repository.*;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member registerMember(Member member) throws MemberRepositoryException;
    void activateMember(Long id) throws MemberRepositoryException;
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member updateMember(Long id, Member member) throws MemberRepositoryException, MemberUpdateException;
    void removeMember(Long id);
    Optional<Member> getMemberWithEmail(String email);
    Optional<Member> getMemberWithUsername(String pattern);
}
