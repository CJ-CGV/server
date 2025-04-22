package com.cj.cgv.domain.reservation;


import com.cj.cgv.domain.reservation.dto.ReservationRes;
import com.cj.cgv.domain.seat.Seat;
import com.cj.cgv.domain.seat.SeatRepository;
import com.cj.cgv.global.common.StatusCode;
import com.cj.cgv.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public ReservationRes createReservation(String userName, Long seatId){
        Seat seat=findSeatBySeatId(seatId);

        if(!seat.getIsReserved())
            seat.soldout();
        else throw new CustomException(StatusCode.SEAT_SOLD_OUT);


        Reservation reservation= Reservation.builder()
                .userName(userName)
                .status(Status.RESERVED)
                .seat(seat)
                .build();


        return ReservationRes.from(reservationRepository.save(reservation));
    }

    public List<ReservationRes> getReservationList(String userName){
        List<Reservation> reservations= reservationRepository.findAllByUserName(userName);

        return reservations.stream()
                .map(ReservationRes::from)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long reservationId){
        Reservation reservation= reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(StatusCode.RESERVATION_NOT_EXIST));

        if(reservation.getStatus()==Status.RESERVED)
            reservation.cancel();
        else throw new CustomException(StatusCode.RESERVATION_IS_DELETED);
    }
    

    private Seat findSeatBySeatId(Long seatId){
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(StatusCode.SEAT_NOT_EXIST));
    }

}
