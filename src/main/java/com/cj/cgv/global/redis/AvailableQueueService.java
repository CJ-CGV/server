package com.cj.cgv.global.redis;

import com.cj.cgv.global.redis.dto.QueueRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailableQueueService {
    private final StringRedisTemplate redisTemplate;
    private final WaitingQueueService waitingQueueService;

    private static final long ALLOW_DURATION_MILLIS = 60 * 1000; // 60초

    // 유저를 available queue에 추가 (입장 허용)
    public void allowUser(String username, Long scheduleId) {
        String key = getAvailableQueueKey(scheduleId);
        long expireAt = System.currentTimeMillis() + ALLOW_DURATION_MILLIS;
        redisTemplate.opsForZSet().add(key, username, expireAt);
    }

    // 유저가 입장 가능한지 확인
    public QueueRes isUserAllowed(String username, Long scheduleId) {
        String key = getAvailableQueueKey(scheduleId);
        Double expireAt = redisTemplate.opsForZSet().score(key, username);

        QueueRes queueRes;
        if (expireAt != null && expireAt > System.currentTimeMillis()) {
            queueRes = new QueueRes(true, null); // 입장 가능
        } else {
            queueRes = waitingQueueService.getUserOrder(username, scheduleId); // 대기열 순서 반환
        }
        return queueRes;
    }

    // 유저를 available queue에서 제거
    public void exitQueue(String username, Long scheduleId) {
        String key = getAvailableQueueKey(scheduleId);
        redisTemplate.opsForZSet().remove(key, username);
    }

    // 현재 시간 기준으로 만료된 유저 가져오기
    public Set<String> getExpiredUsers(Long scheduleId) {
        String key = getAvailableQueueKey(scheduleId);
        long now = System.currentTimeMillis();
        return redisTemplate.opsForZSet().rangeByScore(key, 0, now);
    }

    public void removeUsers(Long scheduleId, Set<String> usernames) {
        String key = getAvailableQueueKey(scheduleId);
        redisTemplate.opsForZSet().remove(key, usernames.toArray());
    }

    // 모든 스케줄에 대한 scheduleId 반환
    public Set<Long> getAllScheduleIds() {
        Set<String> keys = redisTemplate.keys("available_queue:schedule:*");
        return keys.stream()
                .map(key -> Long.parseLong(key.replace("available_queue:schedule:", "")))
                .collect(Collectors.toSet());
    }

    public void deleteAvailableQueueKey(Long scheduleId) {
        redisTemplate.delete(getAvailableQueueKey(scheduleId));
    }

    // 전체 유저 가져오기 (스케쥴러에서 사용)
    public Set<String> getAllUsers(Long scheduleId) {
        String key = getAvailableQueueKey(scheduleId);
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    private String getAvailableQueueKey(Long scheduleId) {
        return "available_queue:schedule:" + scheduleId;
    }
}
