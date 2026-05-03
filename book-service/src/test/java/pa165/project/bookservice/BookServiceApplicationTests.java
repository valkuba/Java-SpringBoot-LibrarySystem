package pa165.project.bookservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceApplicationTests {
    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;
    @Test
    void contextLoads() {
    }

}
