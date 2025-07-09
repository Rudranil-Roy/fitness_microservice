package com.fitness.gateway.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId){
        log.info("Calling User Validation api  for {}", userId);
        return userServiceWebClient.get()
                .uri("/api/v1/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    return Mono.error(new RuntimeException(e.getMessage()));
                });
    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Calling User registration api  for email {}", registerRequest.getEmail());
        return userServiceWebClient.post()
                .uri("/api/v1/users/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    return Mono.error(new RuntimeException(e.getMessage()));
                });
    }
}
