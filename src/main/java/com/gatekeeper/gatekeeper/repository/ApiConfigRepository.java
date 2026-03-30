package com.gatekeeper.gatekeeper.repository;

import com.gatekeeper.gatekeeper.model.ApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiConfigRepository extends JpaRepository<ApiConfig, Long> {
    Optional<ApiConfig> findByProxyKey(String proxyKey);
}
