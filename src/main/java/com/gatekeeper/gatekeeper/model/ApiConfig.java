package com.gatekeeper.gatekeeper.model;
import com.gatekeeper.gatekeeper.enums.AuthType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "api_configs")
@Getter
@Setter
public class ApiConfig {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String baseUrl;
    private String apiKey;
    @Enumerated(EnumType.STRING)
    private AuthType authType;
    // Only used when authType = QUERY_PARAM  (e.g. "key", "appid", "api_key")
    @Column(nullable = true)
    private String authParamName;
    // Only used when authType = CUSTOM_HEADER (e.g. "x-api-key", "x-rapidapi-key")
    @Column(nullable = true)
    private String authHeaderName;

    private String proxyKey;

    @Column(name = "user_id")
    private Long userId;

    public ApiConfig() {}
}