package pa165.project.notificationstatsservice.config;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;


import java.util.Objects;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/api/notifications/**").access(hasScope("test_read"))
                        .requestMatchers(HttpMethod.GET, "/api/stats/**").access(hasScope("test_read"))
                        .requestMatchers(HttpMethod.POST, "/api/notifications/**").access(hasScope("test_write"))
                        .requestMatchers(HttpMethod.DELETE, "/api/notifications/**").access(hasScope("test_write"))
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties props) {
        String issuer = Objects.requireNonNull(props.getJwt().getIssuerUri());
        String audience = props.getJwt().getAudiences().get(0);
        OAuth2TokenValidator<Jwt> atJwtValidator = JwtValidators.createAtJwtValidator()
                .clientId(audience)
                .audience(audience)
                .issuer(issuer)
                .build();
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
        decoder.setJwtValidator(atJwtValidator);
        return decoder;
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi ->
            openApi.getComponents()
                    .addSecuritySchemes("bearerAuth",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                    )
                    .addSecuritySchemes("MUNI",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.OAUTH2)
                                    .flows(new OAuthFlows()
                                            .authorizationCode(new OAuthFlow()
                                                    .authorizationUrl("https://id.muni.cz/oidc/authorize")
                                                    .tokenUrl("https://id.muni.cz/oidc/token")
                                                    .scopes(new Scopes()
                                                            .addString("openid", "get user identifier")
                                                            .addString("test_read", "read notifications and stats")
                                                            .addString("test_write", "create and delete notifications")
                                                    )
                                            )
                                    )
                    );
    }
}
