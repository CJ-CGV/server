package com.cj.cgv.global.Test;


import com.cj.cgv.domain.reservation.ReservationService;
import com.cj.cgv.domain.reservation.dto.ReservationRes;
import com.cj.cgv.global.common.CommonResponse;
import com.cj.cgv.global.kafka.QueueProducer;
import com.cj.cgv.global.redis.AvailableQueueService;
import com.cj.cgv.global.redis.WaitingQueueService;
import com.cj.cgv.global.redis.dto.QueueRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.cj.cgv.global.common.StatusCode.*;

@Tag(name = "[부하 테스트]", description = "부하 테스트용 Rest api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {
    private final ReservationService reservationService;
    private final WaitingQueueService waitingQueueService;
    private final AvailableQueueService availableQueueService;
    private final QueueProducer queueProducer;

    @Operation(summary = "테스트 예매 생성(서버,DB 부하)", description = "1개의 예매를 생성합니다.")
    @PostMapping("/reservations")
    public ResponseEntity<CommonResponse<ReservationRes>> createReservation(
            @RequestParam String userName,
            @RequestParam Long seatId) {
        return ResponseEntity
                .status(RESERVATION_CREATE.getStatus())
                .body(CommonResponse.from(RESERVATION_CREATE.getMessage(),
                        reservationService.createReservation(userName,seatId)));
    }

    @Operation(summary = "예매 대기열 입장(서버,Redis 부하)", description = "Redis 예매 대기열에 입장합니다.")
    @PostMapping("/enter/redis")
    public ResponseEntity<CommonResponse<QueueRes>> enterQueue(@RequestParam String username, @RequestParam Long scheduleId) {
        waitingQueueService.enterWaitingQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_INSERT.getStatus())
                .body(CommonResponse.from(QUEUE_USER_INSERT.getMessage(),
                        waitingQueueService.getUserOrder(username, scheduleId)));
    }

    // 대기열 입장(카프카)
    @Operation(summary = "예매 대기열 입장(서버,Kafka, Redis 부하)", description = "카프카를 통해 Redis 예매 대기열에 입장합니다.")
    @PostMapping("/enter/kafka")
    public ResponseEntity<CommonResponse<Object>> enterQueueWithKafka(@RequestParam String username, @RequestParam Long scheduleId) {
        queueProducer.sendReservationEvent(scheduleId, username);
        return ResponseEntity
                .status(QUEUE_USER_INSERT.getStatus())
                .body(CommonResponse.from(QUEUE_USER_INSERT.getMessage()));
    }

    @Operation(summary = "예매 대기열 초기화", description = "대기열에 생긴 데이터들을 초기화합니다.")
    @PostMapping("/reset")
    public ResponseEntity<CommonResponse<Object>> enterQueue(@RequestParam Long scheduleId) {
        waitingQueueService.deleteQueueKey(scheduleId);
        availableQueueService.deleteAvailableQueueKey(scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_DELETE.getStatus())
                .body(CommonResponse.from(QUEUE_USER_DELETE.getMessage()));
    }
}
