package pa165.project.memberservice.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pa165.project.memberservice.repository.MemberRepository;
import pa165.project.memberservice.security.SecurityConfig;

import javax.sql.DataSource;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME_NAME)
public class SeedController {

    private final MemberRepository memberRepository;
    private final DataSource dataSource;

    @PostMapping("/seed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(SecurityConfig.SCOPE_ADMIN)
    void seed() {
        if (memberRepository.count() == 0) {
            populate();
        }
    }

    @PostMapping("/reseed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(SecurityConfig.SCOPE_ADMIN)
    void reseed() {
        memberRepository.deleteAll();
        populate();
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(SecurityConfig.SCOPE_ADMIN)
    void clear() {
        memberRepository.deleteAll();
    }

    private void populate() {
        var populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/seed/seed-data.sql"));
        populator.execute(dataSource);
    }
}
