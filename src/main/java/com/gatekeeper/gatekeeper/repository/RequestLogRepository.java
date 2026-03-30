package com.gatekeeper.gatekeeper.repository;

import com.gatekeeper.gatekeeper.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
