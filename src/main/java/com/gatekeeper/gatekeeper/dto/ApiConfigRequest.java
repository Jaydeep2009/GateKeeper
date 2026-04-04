package com.gatekeeper.gatekeeper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfigRequest {
    private String baseUrl;
    private String apiKey;
    private String authType;
    private String authParamName;   // optional — send only for QUERY_PARAM
    private String authHeaderName;  // optional — send only for CUSTOM_HEADER
}
