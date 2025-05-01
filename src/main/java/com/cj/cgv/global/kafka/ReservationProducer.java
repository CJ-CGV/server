package com.cj.cgv.global.kafka;


import com.cj.cgv.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationProducer {
    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

    public void sendReservationRequest(String userName, Long seatId, Long scheduleId) {
        ReservationEvent request = new ReservationEvent(userName, seatId, scheduleId);
        String topic = "reservation-" + scheduleId;
        kafkaTemplate.send(topic, seatId.toString(), request); // seatId 기준 파티셔닝
    }
}
