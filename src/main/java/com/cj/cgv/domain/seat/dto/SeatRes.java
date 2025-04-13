package com.cj.cgv.domain.seat.dto;

import com.cj.cgv.domain.seat.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatRes {
    private final Long id;
    private final Integer rowIndex;
    private final Integer columnIndex;
    private final Boolean isReserved;

    @Builder
    public SeatRes(Long id, Integer rowIndex, Integer columnIndex, Boolean isReserved) {
        this.id = id;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.isReserved = isReserved;
    }

    public static SeatRes from(Seat seat){
        return SeatRes.builder()
                .id(seat.getId())
                .rowIndex(seat.getRowIndex())
                .columnIndex(seat.getColumnIndex())
                .isReserved(seat.getIsReserved())
                .build();
    }
}
