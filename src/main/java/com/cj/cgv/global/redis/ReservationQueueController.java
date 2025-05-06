package com.cj.cgv.global.redis;


import com.cj.cgv.global.common.CommonResponse;
import com.cj.cgv.global.kafka.QueueProducer;
import com.cj.cgv.global.redis.dto.QueueRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cj.cgv.global.common.StatusCode.*;


@Tag(name = "[예매 대기열 관리]", description = "예매 대기열 입장/퇴장 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/queue")
public class ReservationQueueController {
    private final WaitingQueueService waitingQueueService;
    private final AvailableQueueService availableQueueService;
    private final QueueProducer queueProducer;

    // 대기열 입장
    @Operation(summary = "예매 대기열 입장", description = "Redis 예매 대기열에 입장합니다.")
    @PostMapping("/enter/redis")
    public ResponseEntity<CommonResponse<QueueRes>> enterQueue(@RequestParam String username, @RequestParam Long scheduleId) {
        waitingQueueService.enterWaitingQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_INSERT.getStatus())
                .body(CommonResponse.from(QUEUE_USER_INSERT.getMessage(),
                        waitingQueueService.getUserOrder(username, scheduleId)));
    }

    // 대기열 입장(카프카)
    @Operation(summary = "예매 대기열 입장(카프카)", description = "카프카를 통해 Redis 예매 대기열에 입장합니다.")
    @PostMapping("/enter/kafka")
    public ResponseEntity<CommonResponse<Object>> enterQueueWithKafka(@RequestParam String username, @RequestParam Long scheduleId) {
        queueProducer.sendReservationEvent(scheduleId, username);
        return ResponseEntity
                .status(QUEUE_USER_INSERT.getStatus())
                .body(CommonResponse.from(QUEUE_USER_INSERT.getMessage()));
    }

    // 대기열 퇴장
    @Operation(summary = "예매 대기열 퇴장", description = "예매 대기열에서 퇴장합니다. 브라우저를 종료하거나, 대기열에서 나가야할 때 쓰면 됩니다.")
    @PostMapping("/exit")
    public ResponseEntity<CommonResponse<Object>> exitQueue(@RequestParam String username, @RequestParam Long scheduleId) {
        waitingQueueService.exitQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_DELETE.getStatus())
                .body(CommonResponse.from(QUEUE_USER_DELETE.getMessage()));
    }

    // 예매 가능 여부 확인 (available queue)
    @Operation(summary = "예매 가능 여부를 확인합니다.", description = "예매 가능 여부를 반환합니다. isAvailable이 true이면 좌석선택창으로 가면 됩니다. <br>" +
            "아직 예매 대기열에 유저가 존재하면 대기열 순번도 같이 반환합니다.")
    @GetMapping("/availabe")
    public ResponseEntity<CommonResponse<QueueRes>> canReserve(@RequestParam String username, @RequestParam Long scheduleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.from(RESERVATION_CREATE.getMessage(),
                        availableQueueService.isUserAllowed(username, scheduleId)));
    }

    // 예매 완료 처리 (available queue에서 제거)
    @Operation(summary = "좌석 예매 성공시 예매 가능 상태 초기화", description = "좌석 예매 성공 후 사용자의 해당 영화 스케쥴 예매 가능 상태를 초기화합니다.  <br>" +
            "즉, 다시 예매하려면 대기열에 입장해야합니다.")
    @PostMapping("/complete-reservation")
    public ResponseEntity<CommonResponse<Object>> completeReservation(@RequestParam String username, @RequestParam Long scheduleId) {
        availableQueueService.exitQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_DELETE.getStatus())
                .body(CommonResponse.from(QUEUE_USER_DELETE.getMessage()));
    }
}
