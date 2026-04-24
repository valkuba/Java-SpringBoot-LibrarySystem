package pa165.project.memberservice.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import pa165.project.memberservice.model.Member;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
}