package com.cj.cgv.domain.seat;


import com.cj.cgv.domain.schedule.Schedule;
import com.cj.cgv.domain.schedule.ScheduleRepository;
import com.cj.cgv.domain.seat.dto.SeatReq;
import com.cj.cgv.domain.seat.dto.SeatRes;
import com.cj.cgv.global.common.StatusCode;
import com.cj.cgv.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public List<SeatRes> createSeat(Long scheduleId, SeatReq seatReq) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isEmpty())
            throw new CustomException(StatusCode.SCHEDULE_NOT_EXIST);
        Schedule movieschedule=schedule.get();
        for(int i=1;i<=seatReq.getRow();i++)
            for(int j=1;j<=seatReq.getColumn();j++){
                seatRepository.save(seatReq.toEntity(i,j,movieschedule));
            }
        List<Seat> seats = seatRepository.findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(scheduleId);
        return seats.stream()
                .map(SeatRes::from)
                .toList();
    }

    public List<SeatRes> getSeatList(Long scheduleId) {
        List<Seat> seats = seatRepository.findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(scheduleId);
        return seats.stream()
                .map(SeatRes::from)
                .toList();
    }

    @Transactional
    public void deleteSeatAll(Long scheduleId) {
        seatRepository.deleteAllBySchedule_Id(scheduleId);
    }

}
