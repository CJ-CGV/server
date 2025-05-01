package com.cj.cgv.global.kafka;


import com.cj.cgv.domain.reservation.Reservation;
import com.cj.cgv.domain.reservation.ReservationRepository;
import com.cj.cgv.domain.reservation.Status;
import com.cj.cgv.domain.seat.Seat;
import com.cj.cgv.domain.seat.SeatRepository;
import com.cj.cgv.global.common.StatusCode;
import com.cj.cgv.global.exception.CustomException;
import com.cj.cgv.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @KafkaListener(topicPattern = "reservation-.*", groupId = "reservation-group")
    public void consume(ConsumerRecord<String, ReservationEvent> record) {
        ReservationEvent request = record.value();

        Seat seat= seatRepository.findById(request.getSeatId()).orElseThrow(() -> new CustomException(StatusCode.SEAT_SOLD_OUT));

        if(!seat.getIsReserved())
            seat.soldout();
        else throw new CustomException(StatusCode.SEAT_SOLD_OUT);


        Reservation reservation= Reservation.builder()
                .userName(request.getUserName())
                .status(Status.RESERVED)
                .seat(seat)
                .build();

        reservationRepository.save(reservation);
    }
}
