package com.cj.cgv.domain.seat;

import com.cj.cgv.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_index",nullable = false)
    private Integer rowIndex;

    @Column(name = "column_index",nullable = false)
    private Integer columnIndex;

    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;


    @Builder
    public Seat(Integer rowIndex, Integer columnIndex, Boolean isReserved, Schedule schedule) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.isReserved = isReserved;
        this.schedule = schedule;
    }

    public void soldout(){this.isReserved=true;}

    public void cancle(){this.isReserved=false;}
}
