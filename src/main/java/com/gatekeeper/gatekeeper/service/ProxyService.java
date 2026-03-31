package com.gatekeeper.gatekeeper.service;

import com.gatekeeper.model.ApiConfig;
import com.gatekeeper.repository.ApiConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class ProxyService {

    private final ApiConfigRepository apiConfigRepository;
    private final WebClient.Builder webClientBuilder;

    public ResponseEntity<String> forwardRequest(
            String proxyKey,
            String remainingPath,
            HttpMethod method,
            HttpHeaders headers,
            String body
    ) {

        // 🔹 1. Find config
        ApiConfig config = apiConfigRepository.findByProxyKey(proxyKey)
                .orElseThrow(() -> new RuntimeException("Invalid proxy key"));

        // 🔹 2. Build URL
        String targetUrl = config.getBaseUrl() + "/" + remainingPath;

        WebClient webClient = webClientBuilder.build();

        // 🔹 3. Make request
        WebClient.RequestBodySpec request = webClient
                .method(method)
                .uri(targetUrl)
                .headers(h -> {
                    h.addAll(headers);

                    // Inject API Key
                    if ("API_KEY".equals(config.getAuthType())) {
                        h.set("Authorization", "Bearer " + config.getApiKey());
                    }
                });

        WebClient.ResponseSpec responseSpec;

        if (body != null && !body.isEmpty()) {
            responseSpec = request.bodyValue(body).retrieve();
        } else {
            responseSpec = request.retrieve();
        }

        // 🔹 4. Get response
        String responseBody = responseSpec.bodyToMono(String.class).block();

        return ResponseEntity.ok(responseBody);
    }
}