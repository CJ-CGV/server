package com.cj.cgv.domain.reservation;


import com.cj.cgv.domain.seat.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;


    @Builder
    public Reservation(String userName, Status status, Seat seat) {
        this.userName= userName;
        this.status = status;
        this.seat = seat;
    }

    public void cancel(){ this.status=Status.CANCELED; }
}
