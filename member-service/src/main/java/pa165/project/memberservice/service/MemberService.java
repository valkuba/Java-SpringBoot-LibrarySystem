package pa165.project.memberservice.service;

import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.exception.MemberUpdateException;
import pa165.project.memberservice.service.exception.MemberServiceException;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member registerMember(Member member) throws MemberServiceException;
    void activateMember(Long id) throws MemberServiceException;
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member updateMember(Member member) throws MemberServiceException, MemberUpdateException;
    void removeMember(Long id);
    Optional<Member> getMemberWithEmail(String email);
    Optional<Member> getMemberWithUsername(String pattern);
}
