package com.cj.cgv.domain.seat;

import com.cj.cgv.domain.seat.dto.SeatReq;
import com.cj.cgv.domain.seat.dto.SeatRes;
import com.cj.cgv.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cj.cgv.global.common.StatusCode.*;

@Tag(name = "[영화 좌석]", description = "영화 좌석 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "영화 좌석 생성", description = "해당 영화일정(scheduleId)의 좌석을 생성합니다.<br>"+
    "행 * 열 만큼의 좌석을 생성합니다. 예) 행=3,열=3 -> (1,1),(1,2),(1,3),(2,1)... (3,3) <br>" +
    "좌석을 다시 생성하려면, 무조건 좌석을 삭제 한 다음 생성 하세요!! 겹칠 수가 있어요!!")
    @PostMapping
    public ResponseEntity<CommonResponse<List<SeatRes>>> createSeat(
            @RequestParam Long scheduleId,
            @RequestBody SeatReq seatReq){
        return ResponseEntity
                .status(SEAT_CREATE.getStatus())  // 여기는 계속 같은 상태 코드를 사용하시면 됩니다.
                .body(CommonResponse.from(SEAT_CREATE.getMessage(),
                        seatService.createSeat(scheduleId, seatReq)));
    }

    @Operation(summary = "영화 좌석 조회", description = "해당 영화일정(scheduleId)의 모든 좌석을 조회합니다.<br>"+
    "좌석은 행,열 순으로 정렬되어 보여집니다. 예) (1,1),(1,2),(1,3),(2,1)...(3,3)")
    @GetMapping
    public ResponseEntity<CommonResponse<List<SeatRes>>> getSeatList(
            @RequestParam Long scheduleId) {
        return ResponseEntity
                .status(SEAT_FOUND.getStatus())
                .body(CommonResponse.from(SEAT_FOUND.getMessage(),
                        seatService.getSeatList(scheduleId)));
    }

    @Operation(summary = "영화 좌석 전체 삭제", description = "해당 영화일정(scheduleId)의 전체 좌석을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<Object> deleteSeat(@RequestParam Long scheduleId) {
        seatService.deleteSeatAll(scheduleId);
        return ResponseEntity
                .status(SEAT_DELETE.getStatus())  // 동일한 상태 코드 사용
                .body(CommonResponse.from(SEAT_DELETE.getMessage()));
    }

}
