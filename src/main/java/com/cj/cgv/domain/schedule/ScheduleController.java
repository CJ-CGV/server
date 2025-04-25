package com.cj.cgv.domain.schedule;

import com.cj.cgv.domain.schedule.dto.ScheduleReq;
import com.cj.cgv.domain.schedule.dto.ScheduleRes;
import com.cj.cgv.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cj.cgv.global.common.StatusCode.*;

@Tag(name = "[영화 일정]", description = "영화 일정 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "영화 일정 생성", description = "해당 영화(movieId)의 일정을 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponse<ScheduleRes>> createSchedule(
            @RequestParam Long movieId,
            @RequestBody ScheduleReq scheduleReq){
        return ResponseEntity
                .status(SCHEDULE_CREATE.getStatus())
                .body(CommonResponse.from(SCHEDULE_CREATE.getMessage()
                        ,scheduleService.createSchedule(movieId,scheduleReq)));
    }

    @Operation(summary = "영화 일정 조회", description = "해당 영화(movieId)의 모든 일정을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<ScheduleRes>>> getScheduleList(
            @RequestParam Long movieId) {
        return ResponseEntity
                .status(SCHEDULE_FOUND.getStatus())
                .body(CommonResponse.from(SCHEDULE_FOUND.getMessage(),
                        scheduleService.getScheduleList(movieId)));
    }

    @Operation(summary = "영화 일정 삭제", description = "해당 영화 일정을 삭제합니다.")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Object> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity
                .status(SCHEDULE_DELETE.getStatus())
                .body(CommonResponse.from(SCHEDULE_DELETE.getMessage()));
    }
}
