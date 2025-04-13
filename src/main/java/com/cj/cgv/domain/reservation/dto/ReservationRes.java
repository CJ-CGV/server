package com.cj.cgv.domain.reservation.dto;

import com.cj.cgv.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationRes {
    private final Long id;
    private final String status;
    private final String movieTitle;
    private final LocalDate date;
    private final LocalTime time;
    private final Integer runningTime;
    private final Integer rowIndex;
    private final Integer columnIndex;

    @Builder
    public ReservationRes(Long id, String status, String movieTitle, LocalDate date, LocalTime time, Integer runningTime, Integer rowIndex, Integer columnIndex) {
        this.id = id;
        this.status = status;
        this.movieTitle = movieTitle;
        this.date = date;
        this.time = time;
        this.runningTime = runningTime;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public static ReservationRes from(Reservation reservation){
        return ReservationRes.builder()
                .id(reservation.getId())
                .status(reservation.getStatus().toString())
                .movieTitle(reservation.getSeat().getSchedule().getMovie().getTitle())
                .date(reservation.getSeat().getSchedule().getDate())
                .time(reservation.getSeat().getSchedule().getTime())
                .runningTime(reservation.getSeat().getSchedule().getRunningTime())
                .rowIndex(reservation.getSeat().getRowIndex())
                .columnIndex(reservation.getSeat().getColumnIndex())
                .build();
    }
}
