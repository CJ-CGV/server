package com.cj.cgv.global.redis;

import com.cj.cgv.global.common.StatusCode;
import com.cj.cgv.global.exception.CustomException;
import com.cj.cgv.global.redis.dto.QueueRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingQueueService {
    private final StringRedisTemplate redisTemplate;

    // 유저를 대기열에 추가
    public void enterWaitingQueue(String username, Long scheduleId) {
        String queueKey = getWaitingQueueKey(scheduleId);
        double timestamp = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(queueKey, username, timestamp);
    }

    // 유저를 대기열에 추가(Kafka offset 이용)
    public void enterWaitingQueueWithKafka(Long offset, String username, Long scheduleId) {
        String queueKey = getWaitingQueueKey(scheduleId);
        redisTemplate.opsForZSet().add(queueKey, username, offset);

        log.info("대기열 진입: 사용자={}, 오프셋={}, 스케줄ID={}", username, offset, scheduleId);
    }

    // 유저의 현재 대기 순서 확인
    public QueueRes getUserOrder(String username, Long scheduleId) {
        String queueKey = getWaitingQueueKey(scheduleId);
        Long rank = redisTemplate.opsForZSet().rank(queueKey, username);
        if(rank==null)
            throw new CustomException(StatusCode.QUEUE_USER_NOT_EXIST);
        return new QueueRes(false,rank + 1);// 0-based index → 1-based 순서
    }


    public void exitQueue(String username, Long scheduleId) {
        String queueKey = getWaitingQueueKey(scheduleId);
        redisTemplate.opsForZSet().remove(queueKey, username);
    }

    public Set<String> getTopUsers(Long scheduleId, int limit) {
        String queueKey = getWaitingQueueKey(scheduleId);
        return redisTemplate.opsForZSet().range(queueKey, 0, limit - 1);
    }

    public void removeUsers(Long scheduleId, Set<String> usernames) {
        String queueKey = getWaitingQueueKey(scheduleId);
        redisTemplate.opsForZSet().remove(queueKey, usernames.toArray());
    }

    // 모든 스케줄에 대한 scheduleId 반환
    public Set<Long> getAllScheduleIds() {
        Set<String> keys = redisTemplate.keys("waiting_queue:schedule:*");
        return keys.stream()
                .map(key -> Long.parseLong(key.replace("waiting_queue:schedule:", "")))
                .collect(Collectors.toSet());
    }

    public void deleteQueueKey(Long scheduleId) {
        redisTemplate.delete(getWaitingQueueKey(scheduleId));
    }

    public Long getQueueSize(Long scheduleId) {
        String queueKey = getWaitingQueueKey(scheduleId);
        return redisTemplate.opsForZSet().size(queueKey);
    }

    // sortedSet에 넣을 키 반환
    private String getWaitingQueueKey(Long scheduleId) {
        return "waiting_queue:schedule:" + scheduleId;
    }
}
