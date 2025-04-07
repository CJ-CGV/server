package com.cj.cgv.domain.seat;

import com.cj.cgv.domain.member.Member;
import com.cj.cgv.domain.movie.Movie;
import com.cj.cgv.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer row;

    @Column(nullable = false)
    private Integer column;

    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;


    @Builder
    public Seat(Integer row, Integer column, Boolean isReserved, Schedule schedule) {
        this.row = row;
        this.column = column;
        this.isReserved = isReserved;
        this.schedule = schedule;
    }
}
