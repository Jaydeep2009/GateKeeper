package com.gatekeeper.gatekeeper.model;
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
    private String authType;
    private String proxyKey;

    @Column(name = "user_id")
    private Long userId;

    public ApiConfig() {}
}