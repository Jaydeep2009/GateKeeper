package com.gatekeeper.gatekeeper.controller;

import com.gatekeeper.gatekeeper.service.ProxyService;
import com.gatekeeper.gatekeeper.service.RateLimiterService;
import com.gatekeeper.gatekeeper.service.RequestLogService;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proxy")
@AllArgsConstructor
public class ProxyController {

    private final ProxyService proxyService;
    private final RateLimiterService rateLimiterService;
    private final RequestLogService requestLogService;

    @RequestMapping(value = {"/{proxyKey}", "/{proxyKey}/**"})
    public ResponseEntity<String> handleRequest(
            @PathVariable String proxyKey,
            HttpMethod method,
            HttpServletRequest request,
            @RequestBody(required = false) String body,
            @RequestHeader HttpHeaders headers
    ) {

        // ✅ FIXED — strip /proxy/{proxyKey}/ to get the remaining path
        String path = request.getRequestURI();
        String prefix = "/proxy/" + proxyKey + "/";
        String remainingPath = "";

        if (path.startsWith(prefix)) {
            remainingPath = path.substring(prefix.length());
        }

        // 🔹 TODO: Replace with real userId (JWT later)
        Long userId = 1L;

        // 🔹 Rate limiting
        if (!rateLimiterService.isAllowed(userId)) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }

        rateLimiterService.recordRequest(userId);

        try {
            ResponseEntity<String> response = proxyService.forwardRequest(
                    proxyKey,
                    remainingPath,
                    method,
                    headers,
                    body
            );

            requestLogService.log(userId, path, response.getStatusCode().value());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            requestLogService.log(userId, path, 500);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}