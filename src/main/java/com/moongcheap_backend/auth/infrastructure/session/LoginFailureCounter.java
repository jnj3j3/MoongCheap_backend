package com.moongcheap_backend.auth.infrastructure.session;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LoginFailureCounter {

    private static final String KEY_PREFIX = "moongcheap:login-fail:";

    private final StringRedisTemplate redis;

    @Value("${moongcheap.security.login-failure.max-attempts}")
    private int maxAttempts;

    @Value("${moongcheap.security.login-failure.lock-duration}")
    private Duration lockDuration;

    public boolean isLocked(String loginId) {
        return getCount(KEY_PREFIX + loginId) >= maxAttempts;
    }

    public void recordFailure(String loginId) {
        increment(KEY_PREFIX + loginId, lockDuration);
    }

    public void reset(String loginId) {
        redis.delete(KEY_PREFIX + loginId);
    }

    private int getCount(String key) {
        String v = redis.opsForValue().get(key);
        if (v == null) return 0;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void increment(String key, Duration ttl) {
        Long count = redis.opsForValue().increment(key);
        if (count != null && count == 1L) {
            redis.expire(key, ttl);
        }
    }
}
