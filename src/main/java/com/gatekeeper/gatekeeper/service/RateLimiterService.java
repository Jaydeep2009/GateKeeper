package com.gatekeeper.gatekeeper.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<Long, List<LocalDateTime>> requestMap = new ConcurrentHashMap<>();

    private static final int LIMIT = 3; // Max requests per minute
    private static final int WINDOW_SECONDS = 60; // Time window in minutes

    //check if allowed
    public boolean isAllowed(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> timestamps = requestMap.getOrDefault(userId, new ArrayList<>());

        // Remove timestamps older than the time window
        timestamps.removeIf(timestamp -> timestamp.isBefore(now.minusSeconds(WINDOW_SECONDS)));

        if (timestamps.size() < LIMIT) {
            timestamps.add(now);
            return true;
        } else {
            return false;
        }
    }

    //record request
    public void recordRequest(Long userId) {

        requestMap.putIfAbsent(userId, Collections.synchronizedList( new ArrayList<>()));

        List<LocalDateTime> timestamps = requestMap.get(userId);

        LocalDateTime now = LocalDateTime.now();

        // Clean old timestamps
        timestamps.removeIf(time -> time.isBefore(now.minusSeconds(WINDOW_SECONDS)));

        // Add new request
        timestamps.add(now);
    }
}
