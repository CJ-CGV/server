package com.cj.cgv.global.kafka;


import com.cj.cgv.global.kafka.event.ReservationEvent;
import com.cj.cgv.global.redis.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class QueueConsumer {
    private final WaitingQueueService waitingQueueService;


    @KafkaListener(topicPattern = "reservation-queue", groupId = "reservation-group")
    @Transactional
    public void consume(ConsumerRecord<String, ReservationEvent> record) {
        Long offset= record.offset();
        ReservationEvent request = record.value();

        waitingQueueService.enterWaitingQueueWithKafka(offset, request.getUserName(), request.getScheduleId());
    }
}
