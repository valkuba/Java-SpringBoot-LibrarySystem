package pa165.project.notificationstatsservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberServiceClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${services.member.base-url:http://localhost:8082}")
    private String baseUrl;

    @Value("${services.member.email-path:/api/members/{memberId}}")
    private String emailPath;

    public Optional<String> getRecipientEmail(Long memberId) {
        if (memberId == null) {
            return Optional.empty();
        }

        try {
            RestClient client = restClientBuilder.baseUrl(baseUrl).build();
            Map response = client.get()
                    .uri(emailPath, memberId)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return Optional.empty();
            }

            Object email = response.get("email");
            if (email == null) {
                email = response.get("recipientEmail");
            }

            return Optional.ofNullable(email).map(String::valueOf);
        } catch (Exception ex) {
            log.warn("Cannot resolve member email for memberId={} from member-service: {}", memberId, ex.getMessage());
            return Optional.empty();
        }
    }
}

