package com.gatekeeper.gatekeeper.model;
import jakarta.persistence.*;

@Entity
@Table(name = "api_configs")
public class ApiConfig {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String baseUrl;
    private String apiKey;
    private String authType;
    private String proxyKey;

    // Foreign key
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Constructors
    public ApiConfig() {}

    // Getters & Setters
}