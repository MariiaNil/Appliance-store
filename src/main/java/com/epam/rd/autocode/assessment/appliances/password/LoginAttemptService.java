package com.epam.rd.autocode.assessment.appliances.password;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME_MS = 5 * 60 * 1000; // 5 минут

    private final ConcurrentMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void loginSuccess(String key) {
        attempts.remove(key);
    }

    public void loginFailed(String key) {
        Attempt attempt = attempts.computeIfAbsent(key, k -> new Attempt());
        attempt.increment();

        if (attempt.getCount() >= MAX_ATTEMPTS) {
            attempt.setBlockTime(System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String key) {
        Attempt attempt = attempts.get(key);
        if (attempt == null) return false;

        if (System.currentTimeMillis() - attempt.getBlockTime() > BLOCK_TIME_MS) {
            attempts.remove(key);
            return false;
        }

        return attempt.getCount() >= MAX_ATTEMPTS;
    }

    public int getRemainingAttempts(String key) {
        Attempt attempt = attempts.get(key);
        return attempt != null ? MAX_ATTEMPTS - attempt.getCount() : MAX_ATTEMPTS;
    }

    private static class Attempt {
        private int count;
        private long blockTime;

        void increment() {
            if (count < MAX_ATTEMPTS) {
                count++;
            }
        }

        int getCount() { return count; }
        long getBlockTime() { return blockTime; }
        void setBlockTime(long time) { blockTime = time; }
    }
}