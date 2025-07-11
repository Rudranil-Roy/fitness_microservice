package com.fitness.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtDecoderConfig {
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(
                "http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/certs"
        ).build();
    }
}