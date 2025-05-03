package com.cj.cgv.global.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationEvent {
    private Long scheduleId;
    private String userName;

    public ReservationEvent(Long scheduleId, String userName) {
        this.scheduleId = scheduleId;
        this.userName = userName;
    }
}
