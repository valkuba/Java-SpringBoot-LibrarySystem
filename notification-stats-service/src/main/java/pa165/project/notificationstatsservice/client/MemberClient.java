package pa165.project.notificationstatsservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class MemberClient {

    @Value("${services.member.base-url:http://localhost:8082}")
    private String memberBaseUrl;

    public String resolveRecipientEmail(Long memberId) {
        try {
            MemberLookupResponse member = RestClient.create(memberBaseUrl)
                    .get()
                    .uri("/api/members/{id}", memberId)
                    .retrieve()
                    .body(MemberLookupResponse.class);

            if (member == null || member.email() == null || member.email().isBlank()) {
                return "unknown-email-for-member-" + memberId;
            }
            return member.email();
        } catch (Exception ex) {
            log.warn("Member lookup failed memberId={} reason={}", memberId, ex.getMessage());
            return "unknown-email-for-member-" + memberId;
        }
    }

    private record MemberLookupResponse(String email) {
    }
}

