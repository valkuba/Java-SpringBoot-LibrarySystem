package pa165.project.memberservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import pa165.project.memberservice.model.Member;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        savedMember = em.persistFlushFind(Member.builder()
                .username("john")
                .email("john@test.com")
                .password("secret")
                .build());
    }

    @Test
    void findByEmail_existingEmail_returnsMember() {
        Optional<Member> result = memberRepository.findByEmail("john@test.com");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void findByEmail_unknownEmail_returnsEmpty() {
        Optional<Member> result = memberRepository.findByEmail("nobody@test.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_existingUsername_returnsMember() {
        Optional<Member> result = memberRepository.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john@test.com", result.get().getEmail());
    }

    @Test
    void findByUsername_unknownUsername_returnsEmpty() {
        Optional<Member> result = memberRepository.findByUsername("ghost");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_newMember_assignsIdAndRegistrationDate() {
        Member member = Member.builder()
                .username("jane")
                .email("jane@test.com")
                .password("pass")
                .build();

        Member result = memberRepository.save(member);
        em.flush();

        assertNotNull(result.getId());
        assertNotNull(result.getRegistrationDate());
    }

    @Test
    void save_duplicateEmail_throwsException() {
        Member duplicate = Member.builder()
                .username("other")
                .email("john@test.com") // same email
                .password("pass")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(duplicate));
    }

    @Test
    void save_duplicateUsername_throwsException() {
        Member duplicate = Member.builder()
                .username("john") // same username
                .email("other@test.com")
                .password("pass")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(duplicate));
    }

    @Test
    void findById_existingId_returnsMember() {
        Optional<Member> result = memberRepository.findById(savedMember.getId());

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        Optional<Member> result = memberRepository.findById(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllMembers() {
        em.persistFlushFind(Member.builder()
                .username("jane")
                .email("jane@test.com")
                .password("pass")
                .build());

        List<Member> result = (List<Member>) memberRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void deleteById_existingMember_removesFromDatabase() {
        Long id = savedMember.getId();

        memberRepository.deleteById(id);
        em.flush();

        assertFalse(memberRepository.findById(id).isPresent());
    }
}
