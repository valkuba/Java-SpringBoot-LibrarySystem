package pa165.project.memberservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MemberServiceApplicationTests {
    @MockitoBean
    JwtDecoder jwtDecoder;
    @Test
    void contextLoads() {
    }

}
