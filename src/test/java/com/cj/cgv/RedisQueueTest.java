package com.cj.cgv;


import com.cj.cgv.global.redis.AvailableQueueService;
import com.cj.cgv.global.redis.WaitingQueueService;
import com.cj.cgv.global.redis.dto.QueueRes;
import com.cj.cgv.global.scheduler.ReservationQueueScheduler;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisQueueTest {
    @Autowired
    private ReservationQueueScheduler reservationQueueScheduler;

    @Autowired
    private WaitingQueueService waitingQueueService;

    @Autowired
    private AvailableQueueService availableQueueService;


    private static final Long SCHEDULE_ID = 1L;
    private static final int USER_COUNT = 1000;

    @BeforeEach
    public void setUp() {
        // 대기열에 1000명 추가
        IntStream.rangeClosed(1, USER_COUNT)
                .forEach(i -> waitingQueueService.enterWaitingQueue("user" + i, SCHEDULE_ID));
    }

    @AfterEach
    public void tearDown() {
        // 대기열 & 입장열 키 삭제로 초기화
        waitingQueueService.deleteQueueKey(SCHEDULE_ID);
        availableQueueService.deleteAvailableQueueKey(SCHEDULE_ID);
    }

    @Test
    @Order(1)
    public void testUserEnterWaitingQueue() {
        Long queueSize = waitingQueueService.getQueueSize(SCHEDULE_ID);
        assertThat(queueSize).isEqualTo(USER_COUNT);
    }

    @Test
    @Order(2)
    public void testMoveUsersToAvailableQueue() {
        reservationQueueScheduler.moveUsersFromWaitingToAvailable();

        // available queue에 100명, waiting queue에 900명 남아야 함
        Set<String> availableUsers = availableQueueService.getAllUsers(SCHEDULE_ID);
        Long remainingWaiting = waitingQueueService.getQueueSize(SCHEDULE_ID);

        assertThat(availableUsers.size()).isEqualTo(100);
        assertThat(remainingWaiting).isEqualTo(900);
    }

    @Test
    @Order(3)
    public void testIsUserAllowed() {
        reservationQueueScheduler.moveUsersFromWaitingToAvailable();

        QueueRes allowed = availableQueueService.isUserAllowed("user1", SCHEDULE_ID);
        QueueRes waiting = availableQueueService.isUserAllowed("user150", SCHEDULE_ID);

        assertThat(allowed.getIsAvailable()).isTrue();
        assertThat(waiting.getIsAvailable()).isFalse();
    }

    @Test
    @Order(4)
    public void testRemoveExpiredUsers() throws InterruptedException {
        reservationQueueScheduler.moveUsersFromWaitingToAvailable();

        // 입장 허용 후 5초 대기 (테스트용으로 5초뒤 만료로 설정함)
        Thread.sleep(5000);

        // 지금은 그냥 스케쥴러 실행 확인
        reservationQueueScheduler.removeExpiredUsersFromAvailableQueue();

        // 큐에 없어야함.
        Set<String> stillAllowed = availableQueueService.getAllUsers(SCHEDULE_ID);
        assertThat(stillAllowed.size()).isEqualTo(0);
    }
}
