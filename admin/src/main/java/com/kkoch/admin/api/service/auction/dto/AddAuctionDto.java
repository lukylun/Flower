package com.kkoch.admin.api.service.auction.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddAuctionDto {

    private LocalDateTime startTime;
    private int code;

    @Builder
    private AddAuctionDto(LocalDateTime startTime, int code) {
        this.startTime = startTime;
        this.code = code;
    }
}
