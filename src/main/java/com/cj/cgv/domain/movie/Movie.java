package com.cj.cgv.domain.movie;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "goods_count", nullable = false)
    private Long goodsCount;

    @Builder
    public Movie(String title, LocalDate releaseDate, Long goodsCount) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.goodsCount = goodsCount;
    }

    public void reduceGoodsCount(){ this.goodsCount--; }
}
