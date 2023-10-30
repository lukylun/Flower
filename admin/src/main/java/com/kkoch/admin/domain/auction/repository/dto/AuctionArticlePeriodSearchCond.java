package com.kkoch.admin.domain.auction.repository.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AuctionArticlePeriodSearchCond {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String code;
    private String type;
    private String name;
    private String region;

    @Builder
    private AuctionArticlePeriodSearchCond(LocalDate startDateTime, LocalDate endDateTime, String code, String type, String name, String region) {
        this.endDateTime = endDateTime.plusDays(1).atStartOfDay();
        this.startDateTime = startDateTime.atStartOfDay();
        this.code = code;
        this.type = type;
        this.name = name;
        this.region = region;
    }

    public static AuctionArticlePeriodSearchCond of(LocalDate startDateTime, LocalDate endDateTime, String code, String type, String name, String region) {
        return new AuctionArticlePeriodSearchCond(startDateTime, endDateTime, code, type, name, region);
    }
}
