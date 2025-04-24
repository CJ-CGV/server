package com.cj.cgv.domain.seat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(Long scheduleId);
    Optional<Seat> findBySchedule_IdAndRowIndexAndColumnIndex(Long scheduleId,Long rowIndex, Long columnIndex);

    void deleteAllBySchedule_Id(Long scheduleId);

}
