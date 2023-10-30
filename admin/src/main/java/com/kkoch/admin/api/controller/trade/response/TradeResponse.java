package com.kkoch.admin.api.controller.trade.response;

import com.kkoch.admin.domain.Grade;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class TradeResponse {

    private String type;
    private String name;

    private String grade;
    private int count;
    private int bidPrice;
    private String bidTime;
    private String region;

    @Builder
    public TradeResponse(String type, String name, Grade grade, int count, int bidPrice, LocalDateTime bidTime, String region) {
        this.type = type;
        this.name = name;
        this.grade = grade.getText();
        this.count = count;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
        this.region = region;
    }
}
