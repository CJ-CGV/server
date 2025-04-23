package com.cj.cgv.domain.reservation;

import com.cj.cgv.domain.seat.Seat;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByUserName(String userName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)  // x-lock 설정
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    @QueryHints(
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")  // 락 못 잡으면 바로 예외
    )
    Optional<Seat> findSeatBySeatIdWithRock(@Param("seatId") Long seatId);


}
