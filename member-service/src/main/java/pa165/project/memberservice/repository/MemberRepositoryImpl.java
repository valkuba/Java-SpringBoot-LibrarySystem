package pa165.project.memberservice.repository;

import org.springframework.stereotype.Repository;
import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.exception.MemberUpdateException;
import pa165.project.memberservice.repository.exception.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    // we dont use JPA in the M1 so instead use HashMap
    private final Map<Long, Member> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    @Override
    public void register(Member member) throws MemberRepositoryException {
        if(store.containsKey(member.getId()))
            throw new MemberIdAlreadyExistsException(member.getId());
        if(store.values().stream().anyMatch(m -> m.getUsername().equals(member.getUsername()))){
            throw new MemberNameAlreadyExistsException(member.getUsername());
        }
        if(store.values().stream().anyMatch(m -> m.getEmail().equals(member.getEmail()))){
            throw new EmailAlreadyRegisteredException(member.getEmail());
        }
        member.setId(idGenerator.getAndIncrement());
        member.setRegistrationDate(new Date());
        store.put(member.getId(), member);
    }

    @Override
    public void activateUser(Long id) throws MemberRepositoryException {
        var memberOpt = findById(id);
        if(memberOpt.isEmpty()){
            throw new MemberAlreadyActivatedException(id);
        }
        var member = memberOpt.get();
        member.setActivated(true);
        store.put(member.getId(), member);
    }

    @Override
    public List<Member> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Member> findById(Long id) {
        return store.values().stream().filter(member -> member.getId().equals(id)).findFirst();
    }

    @Override
    public Member update(Member member) throws MemberRepositoryException, MemberUpdateException {
        // TODO - replace manual checks for member existence and constraints violations with JPA exception handling once JPA is implemented
        if(findById(member.getId()).isEmpty()){
            throw new MemberDoesntExistsException(member.getId());
        }
        var original = store.get(member.getId());
        original.validateChanges(member);
        if(!member.getUsername().equals(original.getUsername()) &&
            findByUsername(member.getUsername()).isPresent()){
            throw new MemberNameAlreadyExistsException(member.getUsername());
        }
        if(!member.getEmail().equals(original.getEmail()) &&
        findByEmail(member.getEmail()).isPresent()){
            throw new EmailAlreadyRegisteredException(member.getEmail());
        }
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return store.values().stream().filter(member -> member.getUsername().equals(username)).findFirst();
    }

}
