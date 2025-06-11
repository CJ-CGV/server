package com.cj.cgv.global.scheduler;


import com.cj.cgv.global.redis.AvailableQueueService;
import com.cj.cgv.global.redis.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationQueueScheduler {
    private final WaitingQueueService waitingQueueService;
    private final AvailableQueueService availableQueueService;

    // 대기열에서 일정 인원 꺼내 입장 허용
    @Scheduled(fixedRate = 5 * 1000)
    public void moveUsersFromWaitingToAvailable() {
        Set<Long> allScheduleIds = waitingQueueService.getAllScheduleIds();

        int moveCount = 1000; // 5초에 1000명씩 입장 허용

        for (Long scheduleId : allScheduleIds) {

            Set<String> topUsers = waitingQueueService.getTopUsers(scheduleId, moveCount);
            if (topUsers == null || topUsers.isEmpty()) {
                return;
            }

            // 입장 허용 → available queue로 이동
            for (String username : topUsers) {
                availableQueueService.allowUser(username, scheduleId);
                log.info("입장 허용: {} (scheduleId={})", username, scheduleId);
            }

            // waiting queue에서 제거
            waitingQueueService.removeUsers(scheduleId, topUsers);
        }
    }

    // 예매가능 열에서 만료된 유저 제거
    @Scheduled(fixedRate = 5 * 1000)
    public void removeExpiredUsersFromAvailableQueue() {
        Set<Long> allScheduleIds = availableQueueService.getAllScheduleIds();

        for (Long scheduleId : allScheduleIds) {

            Set<String> expiredUsers = availableQueueService.getExpiredUsers(scheduleId);
            if (expiredUsers != null && !expiredUsers.isEmpty()) {
                availableQueueService.removeUsers(scheduleId, expiredUsers);
                log.info("만료 제거: {}명 (scheduleId={})", expiredUsers.size(), scheduleId);
            }
        }
    }
}

