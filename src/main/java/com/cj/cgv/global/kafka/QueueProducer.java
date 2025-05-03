package com.cj.cgv.global.kafka;


import com.cj.cgv.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueProducer{
    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

    public void sendReservationEvent(Long scheduleId, String userName) {
        ReservationEvent request = new ReservationEvent(scheduleId, userName);
        kafkaTemplate.send("reservation-queue", scheduleId.toString(), request);

        log.info("Redis 큐 입장 요청 전송됨: 사용자={}, 영화 스케줄ID={}" , userName, scheduleId);
    }
}
