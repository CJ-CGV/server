package com.cj.cgv.global.redis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueRes {
    private final Boolean isAvailable;
    private final Long order;

    @Builder
    public QueueRes(Boolean isAvailable, Long order) {
        this.isAvailable=isAvailable;
        this.order = order;
    }
}
