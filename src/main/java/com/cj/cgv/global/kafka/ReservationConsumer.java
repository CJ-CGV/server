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
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConsumer {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @KafkaListener(topicPattern = "reservation-.*", groupId = "reservation-group")
    @Transactional
    public void consume(ConsumerRecord<String, ReservationEvent> record) {
        ReservationEvent request = record.value();

        Seat seat= seatRepository.findById(request.getSeatId()).orElseThrow(() -> new CustomException(StatusCode.SEAT_SOLD_OUT));

        log.info("예약 이벤트 수신: 사용자={}, 좌석ID={}, 스케줄ID={}", request.getUserName(), seat.getId(), request.getScheduleId());

        if(!seat.getIsReserved())
            seat.soldout();
        else {
            log.warn("이미 예약된 좌석입니다. seatId={}", seat.getId());
            throw new CustomException(StatusCode.SEAT_SOLD_OUT);
        }


        Reservation reservation= Reservation.builder()
                .userName(request.getUserName())
                .status(Status.RESERVED)
                .seat(seat)
                .build();

        reservationRepository.save(reservation);

        log.info("예약 저장 완료: 사용자={}, 좌석ID={}", request.getUserName(), seat.getId());
    }
}
