package com.gatekeeper.gatekeeper.controller;

import com.gatekeeper.gatekeeper.dto.ApiConfigRequest;
import com.gatekeeper.gatekeeper.model.ApiConfig;
import com.gatekeeper.gatekeeper.service.ApiConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configs")
@AllArgsConstructor
public class ApiConfigController {
    private final ApiConfigService apiConfigService;
    //create config
    @PostMapping
    public ResponseEntity<String> createConfigs(
            @RequestBody ApiConfigRequest request,
            @RequestParam Long userId
    ) {
        ApiConfig config = apiConfigService.createApiConfig(request, userId);
        return ResponseEntity.status(201).body(config.getProxyKey());
    }
    //  Get all configs for user
    @GetMapping("/{userId}")
    public ResponseEntity<List<ApiConfig>> getConfigsByUserId(@PathVariable Long userId) {
        List<ApiConfig> configs = apiConfigService.getConfigByUser(userId);
        return ResponseEntity.ok(configs);
    }

    // Delete config
    @DeleteMapping("/{configId}")
    public ResponseEntity<String> deleteConfig(@PathVariable Long configId) {
        apiConfigService.deleteConfig(configId);
        return ResponseEntity.ok("Config deleted successfully");
    }
}

