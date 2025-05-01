package com.cj.cgv.global.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationEvent {
    private String userName;
    private Long seatId;
    private Long scheduleId;

    public ReservationEvent(String userName, Long seatId, Long scheduleId) {
        this.userName = userName;
        this.seatId = seatId;
        this.scheduleId = scheduleId;
    }
}
