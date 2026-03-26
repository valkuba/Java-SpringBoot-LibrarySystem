package pa165.project.memberservice.repository;

import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.MemberUpdateException;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void register(Member member) throws MemberRepositoryException;
    void activateUser(Long id) throws MemberRepositoryException;
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Member update(Member member) throws MemberRepositoryException, MemberUpdateException;
    void deleteById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
}