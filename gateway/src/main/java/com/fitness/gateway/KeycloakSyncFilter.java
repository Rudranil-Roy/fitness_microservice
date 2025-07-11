package com.fitness.gateway;

import com.fitness.gateway.User.RegisterRequest;
import com.fitness.gateway.User.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakSyncFilter implements WebFilter {
    private final UserService userService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest = getUserDetails(token);

        if(userId== null)
            userId=registerRequest.getKeycloakId();

        if(userId!=null && token!=null){
            String finalUserId = userId;
            String finalUserId1 = userId;
            userService.validateUser(userId)
                    .flatMap(exist ->{
                        if(!exist){
                            // regiter user
                            return userService.registerUser(registerRequest)
                                    .then(Mono.empty());
                        } else {
                            log.info("User {} already exist. Skipping Sync", finalUserId);
                            return Mono.empty();
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest request = exchange.getRequest().mutate()
                                .header("X-User-Id", finalUserId1)
                                .build();
                        return chain.filter(exchange.mutate().request(request).build());
                    }));
        }
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.substring(7);
            SignedJWT  signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claimsSet.getStringClaim("email"));
            registerRequest.setPassword("dummy@123");
            registerRequest.setFirstName(claimsSet.getStringClaim("given_name"));
            registerRequest.setLastName(claimsSet.getStringClaim("family_name"));
            registerRequest.setKeycloakId(claimsSet.getStringClaim("sub"));
            return registerRequest;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
