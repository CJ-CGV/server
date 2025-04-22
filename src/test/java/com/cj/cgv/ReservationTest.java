package com.cj.cgv;

import com.cj.cgv.domain.reservation.ReservationRepository;
import com.cj.cgv.domain.reservation.ReservationService;
import com.cj.cgv.domain.seat.Seat;
import com.cj.cgv.domain.seat.SeatRepository;
import com.cj.cgv.domain.seat.SeatService;
import com.cj.cgv.domain.seat.dto.SeatReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
public class ReservationTest {
    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SeatRepository seatRepository;

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
    @DisplayName("좌석 예매 정합성 테스트")
    void testConcurrentReservation() throws InterruptedException {
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            final String username = "user" + i;

            executorService.submit(() -> {
                try {
                    reservationService.createReservation(username, seatId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("좌석 예약 여부: " + seat.getIsReserved());
        System.out.println("발생한 예외 수: " + exceptions.size());

        assertTrue(seat.getIsReserved(), "좌석은 예약 상태여야 합니다");
        assertEquals(1, successCount.get(), "동시에 하나만 성공해야 합니다");
        assertEquals(numberOfThreads - 1, exceptions.size(), "나머지는 예외가 발생해야 합니다");
    }
}
