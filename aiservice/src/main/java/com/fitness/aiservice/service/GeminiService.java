package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApikey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        Map<String, Object> requestBoy= Map.of(
                "contents", new Object[]{
                    Map.of(
                            "parts", new Object[]{
                                    Map.of("text", question)
                            }
                    )
                }
        );

        return webClient.post()
                .uri(geminiApiUrl+geminiApikey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBoy)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
