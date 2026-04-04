package com.gatekeeper.gatekeeper.service;

import com.gatekeeper.gatekeeper.dto.ApiConfigRequest;
import com.gatekeeper.gatekeeper.enums.AuthType;
import com.gatekeeper.gatekeeper.model.ApiConfig;
import com.gatekeeper.gatekeeper.repository.ApiConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApiConfigService {
    private final ApiConfigRepository apiConfigRepository;

    //create api config
    public ApiConfig createApiConfig(ApiConfigRequest request, Long userId) {
        //Generate a unique proxy key for the API config
        String proxyKey = UUID.randomUUID().toString();

        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setBaseUrl(request.getBaseUrl());
        apiConfig.setApiKey(request.getApiKey());
        apiConfig.setAuthType(AuthType.valueOf(request.getAuthType()));
        apiConfig.setAuthParamName(request.getAuthParamName());
        apiConfig.setAuthHeaderName(request.getAuthHeaderName());
        apiConfig.setProxyKey(proxyKey);
        apiConfig.setUserId(userId);
        return apiConfigRepository.save(apiConfig);
    }

    //get all configs of a user
    public List<ApiConfig> getConfigByUser(Long userId) {
        return apiConfigRepository.findByUserId(userId);
    }

    //delete config
    public void deleteConfig(Long configId){
        apiConfigRepository.deleteById(configId);
    }
}
