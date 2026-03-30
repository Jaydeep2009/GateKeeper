package com.gatekeeper.gatekeeper.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String endpoint;

    private LocalDateTime timestamp;

    private int responseStatus;

    // Constructors
    public RequestLog() {}

    public RequestLog(Long userId, String endpoint, LocalDateTime timestamp, int responseStatus) {
        this.userId = userId;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.responseStatus = responseStatus;
    }

    // Getters & Setters
}
