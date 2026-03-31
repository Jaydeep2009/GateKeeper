package com.gatekeeper.gatekeeper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfigRequest {
    private String baseUrl;
    private String apiKey;
    private String authType;
    private String userId;
}
