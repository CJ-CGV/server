package com.cj.cgv.global.redis;


import com.cj.cgv.global.common.CommonResponse;
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

    // 대기열 입장
    @Operation(summary = "예매 대기열 입장", description = "예매 대기열에 입장합니다.")
    @PostMapping("/enter")
    public ResponseEntity<CommonResponse<QueueRes>> enterQueue(@RequestParam String username, @RequestParam Long scheduleId) {
        waitingQueueService.enterWaitingQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_INSERT.getStatus())
                .body(CommonResponse.from(QUEUE_USER_INSERT.getMessage(),
                        waitingQueueService.getUserOrder(username, scheduleId)));
    }

    // 대기열 퇴장
    @Operation(summary = "예매 대기열 퇴장", description = "예매 대기열에서 퇴장합니다.")
    @PostMapping("/exit")
    public ResponseEntity<CommonResponse<Object>> exitQueue(@RequestParam String username, @RequestParam Long scheduleId) {
        waitingQueueService.exitQueue(username, scheduleId);
        return ResponseEntity
                .status(QUEUE_USER_DELETE.getStatus())
                .body(CommonResponse.from(QUEUE_USER_DELETE.getMessage()));
    }

    // 예매 가능 여부 확인 (available queue)
    @Operation(summary = "예매 가능 여부를 확인합니다.", description = "예매 가능 여부를 반환합니다. <br>" +
            "아직 예매 대기열에 유저가 존재하면 대기열 순번도 같이 반환합니다.")
    @GetMapping("/availabe")
    public ResponseEntity<CommonResponse<QueueRes>> canReserve(@RequestParam String username, @RequestParam Long scheduleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.from(RESERVATION_CREATE.getMessage(),
                        availableQueueService.isUserAllowed(username, scheduleId)));
    }

    // 예매 완료 처리 (available queue에서 제거)
    @PostMapping("/complete-reservation")
    public String completeReservation(@RequestParam String username, @RequestParam Long scheduleId) {
        availableQueueService.exitQueue(username, scheduleId);
        return "예매 완료 처리되었습니다.";
    }
}
