package pa165.project.memberservice.security;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    public static final String SECURITY_SCHEME_NAME = "MUNI";
    public static final String SCOPE_VISITOR = "hasAuthority('SCOPE_test_2')";
    public static final String SCOPE_MEMBER = "hasAuthority('SCOPE_test_3')";
    public static final String SCOPE_ADMIN = "hasAuthority('SCOPE_test_4')";

    /**
     * Configure access restrictions to the API.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(x -> x
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Validation of MUNI JWT tokens
     */
    @Bean
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

    /**
     * Add security definitions to generated openapi.yaml.
     * Use Bearer token obtained from the seminar confidential-client.
     */
    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> {
            openApi.getComponents()
                    .addSecuritySchemes(SECURITY_SCHEME_NAME,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Paste a JWT access token obtained from MUNI OIDC "
                                            + "(use the seminar confidential-client to get one)")
                    );
        };
    }
}
