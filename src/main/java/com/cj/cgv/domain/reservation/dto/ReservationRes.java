package com.cj.cgv.domain.reservation.dto;

import com.cj.cgv.domain.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationRes {
    private final Long id;
    private final String status;
    private final String movieTitle;
    private final LocalDate date;
    private final LocalTime time;
    private final Integer runningTime;
    private final Integer rowIndex;
    private final Integer columnIndex;
    private Boolean isGoods;

    @Builder
    public ReservationRes(Long id, String status, String movieTitle, LocalDate date, LocalTime time, Integer runningTime, Integer rowIndex, Integer columnIndex, Boolean isGoods) {
        this.id = id;
        this.status = status;
        this.movieTitle = movieTitle;
        this.date = date;
        this.time = time;
        this.runningTime = runningTime;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.isGoods = isGoods;
    }

    public static ReservationRes from(Reservation reservation, Boolean isGoods){
        return ReservationRes.builder()
                .id(reservation.getId())
                .status(reservation.getStatus().toString())
                .movieTitle(reservation.getSeat().getSchedule().getMovie().getTitle())
                .date(reservation.getSeat().getSchedule().getDate())
                .time(reservation.getSeat().getSchedule().getTime())
                .runningTime(reservation.getSeat().getSchedule().getRunningTime())
                .rowIndex(reservation.getSeat().getRowIndex())
                .columnIndex(reservation.getSeat().getColumnIndex())
                .isGoods(isGoods)
                .build();
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
