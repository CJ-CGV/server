package com.cj.cgv.domain.schedule;


import com.cj.cgv.domain.movie.Movie;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(name = "running_time", nullable = false)
    private Integer runningTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Builder
    public Schedule(LocalDate date, LocalTime time, Integer runningTime, Movie movie) {
        this.date = date;
        this.time = time;
        this.runningTime = runningTime;
        this.movie = movie;
    }
}
