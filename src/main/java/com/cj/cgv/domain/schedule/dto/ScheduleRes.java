package com.cj.cgv.domain.schedule.dto;

import com.cj.cgv.domain.schedule.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleRes {
    private final Long id;
    private final LocalDate date;
    private final LocalTime time;
    private final Integer runningTime;

    @Builder
    public ScheduleRes(Long id, LocalDate date, LocalTime time, Integer runningTime) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.runningTime = runningTime;
    }

    public static ScheduleRes from(Schedule schedule){
        return ScheduleRes.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .runningTime(schedule.getRunningTime())
                .build();
    }
}
