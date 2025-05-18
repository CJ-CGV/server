package com.cj.cgv.domain.seat;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(Long scheduleId);
    Optional<Seat> findBySchedule_IdAndRowIndexAndColumnIndex(Long scheduleId,Long rowIndex, Long columnIndex);

    @Lock(LockModeType.PESSIMISTIC_WRITE)  // x-lock 설정
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    @QueryHints(
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")  // 락 못 잡으면 바로 예외
    )
    Optional<Seat> findBySeatIdWithRock(@Param("seatId") Long seatId);

    void deleteAllBySchedule_Id(Long scheduleId);

}
