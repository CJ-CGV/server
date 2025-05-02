package com.cj.cgv.global.kafka;


import com.cj.cgv.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationProducer {
    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

    public void sendReservationRequest(String userName, Long seatId, Long scheduleId) {
        ReservationEvent request = new ReservationEvent(userName, seatId, scheduleId);
        String topic = "reservation-" + scheduleId;
        kafkaTemplate.send(topic, seatId.toString(), request); // seatId 기준 파티셔닝

        log.info("예약 요청 전송됨: 사용자={}, 좌석ID={}, 스케줄ID={}, 토픽={}",
                userName, seatId, scheduleId, topic);
    }
}
