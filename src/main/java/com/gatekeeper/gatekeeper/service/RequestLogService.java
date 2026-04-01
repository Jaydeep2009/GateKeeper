package com.gatekeeper.gatekeeper.service;

import com.gatekeeper.gatekeeper.model.RequestLog;
import com.gatekeeper.gatekeeper.repository.RequestLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RequestLogService {
    private final RequestLogRepository requestLogRepository;
    public void log(Long userId, String endpoint, int status) {

        RequestLog log = new RequestLog();
        log.setUserId(userId);
        log.setEndpoint(endpoint);
        log.setTimestamp(LocalDateTime.now());
        log.setResponseStatus(status);

        requestLogRepository.save(log);
    }
}
