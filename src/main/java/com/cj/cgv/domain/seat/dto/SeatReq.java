package com.cj.cgv.domain.seat.dto;

import com.cj.cgv.domain.schedule.Schedule;
import com.cj.cgv.domain.seat.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatReq {
    private Integer row;

    private Integer column;

    public Seat toEntity(int row, int column, Schedule schedule){
        return Seat.builder()
                .rowIndex(row)
                .columnIndex(column)
                .isReserved(false)
                .schedule(schedule)
                .build();
    }
}
