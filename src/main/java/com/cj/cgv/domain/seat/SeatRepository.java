package com.cj.cgv.domain.seat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(Long scheduleId);
    void deleteAllBySchedule_Id(Long scheduleId);

}
