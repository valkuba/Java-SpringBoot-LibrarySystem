package pa165.project.memberservice.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pa165.project.memberservice.model.Member;
import pa165.project.memberservice.model.exception.MemberUpdateException;
import pa165.project.memberservice.repository.*;
import pa165.project.memberservice.service.exception.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MeterRegistry meterRegistry;

    @Override
    public Member registerMember(Member member) throws MemberServiceException {
        if(member.getId() != null && memberRepository.findById(member.getId()).isPresent()) {
            throw new MemberIdAlreadyExistsException(member.getId());
        }
        if(memberRepository.findByUsername(member.getUsername()).isPresent()){
            throw new MemberNameAlreadyExistsException(member.getUsername());
        }
        if(memberRepository.findByEmail(member.getEmail()).isPresent()){
            throw new EmailAlreadyRegisteredException(member.getEmail());
        }
        memberRepository.save(member);
        log.info("Member registered: username={}, email={}", member.getUsername(), member.getEmail());
        meterRegistry.counter("member.registered").increment();
        return member;
    }

    @Override
    public void activateMember(Long id) throws MemberServiceException {
        var memberOpt = memberRepository.findById(id);
        if(memberOpt.isEmpty()){
            throw new MemberDoesntExistsException(id);
        }
        var member = memberOpt.get();
        if(member.isActivated()){
            throw new MemberAlreadyActivatedException(member.getId());
        }
        member.setActivated(true);
        memberRepository.save(member);
        meterRegistry.counter("member.activated").increment();
    }

    @Override
    public List<Member> getAllMembers() {
        return StreamSupport.stream(memberRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member updateMember(Member member) throws MemberServiceException, MemberUpdateException {
        var original = getById(member);
        if(original.isEmpty()){
            throw new MemberDoesntExistsException(member.getId());
        }
        original.get().validateChanges(member);
        Member updated;
        // should be already covered by validateChanges, but better safe than sorry
        try{
            updated = memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new MemberUpdateException("Could not update member",e);
        }
        log.info("Member updated: id={}, username={}", updated.getId(), updated.getUsername());
        meterRegistry.counter("member.updated").increment();
        return updated;
    }

    private Optional<Member> getById(Member member) {
        return memberRepository.findById(member.getId());
    }

    @Override
    public void removeMember(Long id) {
        memberRepository.deleteById(id);
        log.info("Member removed: id={}", id);
        meterRegistry.counter("member.removed").increment();
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
