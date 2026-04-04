package com.gatekeeper.gatekeeper.service;

import com.gatekeeper.gatekeeper.enums.AuthType;
import com.gatekeeper.gatekeeper.model.ApiConfig;
import com.gatekeeper.gatekeeper.repository.ApiConfigRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
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
        // 🔹 Build URL
        String targetUrl = getString(remainingPath, config);

// 🔹 Build request with correct auth
        WebClient webClient = webClientBuilder.build();
        WebClient.RequestBodySpec request = webClient
                .method(method)
                .uri(targetUrl)
                .headers(h -> {
                    if (headers.getContentType() != null) {
                        h.setContentType(headers.getContentType());
                    }

                    switch (config.getAuthType()) {

                        case BEARER:
                            h.set("Authorization", "Bearer " + config.getApiKey());
                            break;

                        case CUSTOM_HEADER:
                            String headerName = config.getAuthHeaderName() != null
                                    ? config.getAuthHeaderName()
                                    : "x-api-key"; // fallback default
                            h.set(headerName, config.getApiKey());
                            break;

                        case BASIC_AUTH:
                            // BASIC_AUTH stores "username:password" in apiKey field
                            String encoded = java.util.Base64.getEncoder()
                                    .encodeToString(config.getApiKey().getBytes());
                            h.set("Authorization", "Basic " + encoded);
                            break;

                        case QUERY_PARAM:
                        case NO_AUTH:
                            // Nothing to add to headers
                            break;
                    }
                });

        WebClient.ResponseSpec responseSpec;

        if (body != null && !body.isEmpty()) {
            responseSpec = request.bodyValue(body)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(errorBody -> new RuntimeException(errorBody))
                    );
        } else {
            responseSpec = request.retrieve();
        }

        // 🔹 4. Get response
        String responseBody = responseSpec.bodyToMono(String.class).block();

        return ResponseEntity.ok(responseBody);
    }

    private static @NonNull String getString(String remainingPath, ApiConfig config) {
        String targetUrl;
        // clean any leading slash from remainingPath first
        String cleanPath = remainingPath.startsWith("/")
                ? remainingPath.substring(1)
                : remainingPath;

        if (config.getAuthType() == AuthType.QUERY_PARAM) {
            String paramName = config.getAuthParamName() != null
                    ? config.getAuthParamName()
                    : "key"; // fallback default
            targetUrl = config.getBaseUrl() + "/" + cleanPath + "?" + paramName + "=" + config.getApiKey();
        } else {
            targetUrl = config.getBaseUrl() + "/" + cleanPath;
        }
        return targetUrl;
    }
}