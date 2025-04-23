package com.cj.cgv;

import com.cj.cgv.domain.reservation.ReservationRepository;
import com.cj.cgv.domain.reservation.ReservationService;
import com.cj.cgv.domain.seat.Seat;
import com.cj.cgv.domain.seat.SeatRepository;
import com.cj.cgv.domain.seat.SeatService;
import com.cj.cgv.domain.seat.dto.SeatReq;
import com.cj.cgv.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
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
        int numberOfThreads = 10000; // 요청 수
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();
        List<Long> responseTimes = new ArrayList<>();

        // 테스트 시작 시간 기록
        long testStartTime = System.nanoTime();

        for (int i = 0; i < numberOfThreads; i++) {
            final String username = "user" + i;

            executorService.submit(() -> {
                long startTime = System.nanoTime(); // 시작 시간 기록
                try {
                    reservationService.createReservation(username, seatId);
                    successCount.incrementAndGet();
                } catch (PessimisticLockingFailureException e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }

                } catch (CustomException e) {
                    synchronized (exceptions) {
                        // 다른 예외도 처리
                        exceptions.add(e);
                    }
                } catch (Exception e) {
                    synchronized (exceptions) {
                        // 다른 예외도 처리
                        exceptions.add(e);
                    }
                }

                finally {
                    long endTime = System.nanoTime(); // 종료 시간 기록
                    long responseTime = endTime - startTime; // 응답 시간 계산
                    synchronized (responseTimes) {
                        responseTimes.add(responseTime); // 응답 시간 리스트에 추가
                    }
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 테스트 종료 시간 기록
        long testEndTime = System.nanoTime();

        // 전체 테스트 시간 계산 (단위: 밀리초)
        long totalTestTimeMillis = (testEndTime - testStartTime) / 1_000_000; // nano -> milliseconds


        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("좌석 예약 여부: " + seat.getIsReserved());
        System.out.println("발생한 예외 수: " + exceptions.size());

        // 응답 시간 계산
        List<Long> responseTimeList = new ArrayList<>(responseTimes);
        long fastestResponse = responseTimeList.stream().min(Long::compare).orElse(0L) / 1_000_000; // nanoseconds -> milliseconds
        long slowestResponse = responseTimeList.stream().max(Long::compare).orElse(0L) / 1_000_000; // nanoseconds -> milliseconds
        double averageResponse = responseTimeList.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0; // nanoseconds -> milliseconds


        exceptions.forEach(ex -> System.out.println("예외 종류: " + ex.getClass().getSimpleName()));

        System.out.println("최단 응답 시간: " + fastestResponse + "ms");
        System.out.println("최장 응답 시간: " + slowestResponse + "ms");
        System.out.println("평균 응답 시간: " + averageResponse + "ms");
        // 전체 테스트 시간 출력
        System.out.println("전체 테스트 시간: " + totalTestTimeMillis + "ms");



        assertTrue(seat.getIsReserved(), "좌석은 예약 상태여야 합니다");
        assertEquals(1, successCount.get(), "동시에 하나만 성공해야 합니다");
        assertEquals(numberOfThreads - 1, exceptions.size(), "나머지는 예외가 발생해야 합니다");

    }
}
