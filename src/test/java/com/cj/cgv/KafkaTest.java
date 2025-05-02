package com.cj.cgv;


import com.cj.cgv.domain.reservation.ReservationRepository;
import com.cj.cgv.domain.seat.Seat;
import com.cj.cgv.domain.seat.SeatRepository;
import com.cj.cgv.domain.seat.SeatService;
import com.cj.cgv.domain.seat.dto.SeatReq;
import com.cj.cgv.global.kafka.ReservationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("mirror")
@EmbeddedKafka(partitions = 1, topics = {"reservation-1"})
public class KafkaTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationProducer reservationProducer;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Long seatId;


    @BeforeEach
    void setUp() {
        // 기존 데이터 정리
        reservationRepository.deleteAll();
        seatRepository.deleteAll();

        seatService.createSeat(1L,new SeatReq(3,3));

        seatId=seatRepository.findBySchedule_IdAndRowIndexAndColumnIndex(1L,1L,1L).get().getId();


    }


    @Test
    @DisplayName("Kafka 기반 동시 예약 테스트")
    void testKafkaConcurrentReservation() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final String username = "user" + i;
            executorService.submit(() -> {
                try {
                    reservationProducer.sendReservationRequest(username, seatId, 1L);
                    successCount.incrementAndGet(); // 실제 성공과 무관함 (Kafka는 비동기)
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // Kafka Consumer가 처리할 시간 기다리기 (실제로는 Awaitility나 polling preferred)
        sleep(3000);

        Seat seat = seatRepository.findById(seatId).orElseThrow();

        System.out.println("총 요청 수: " + threadCount);
        System.out.println("예약된 좌석 ID: " + seat.getId());
        System.out.println("좌석 예약 여부: " + seat.getIsReserved());
        System.out.println("성공한 사용자 수: " + reservationRepository.count());

        assertTrue(seat.getIsReserved(), "좌석은 예약 상태여야 합니다.");
        assertEquals(1, reservationRepository.count(), "오직 1건의 예약만 존재해야 합니다.");
    }
}
