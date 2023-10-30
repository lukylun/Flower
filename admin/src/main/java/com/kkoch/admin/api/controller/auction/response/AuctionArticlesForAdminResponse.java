package com.kkoch.admin.api.controller.auction.response;

import com.kkoch.admin.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
@NoArgsConstructor
public class AuctionArticlesForAdminResponse {

    // 식물
    private String code;
    private String type;
    private String name;

    private String grade;
    private int count;
    private int bidPrice;
    private String bidTime;
    private String region;
    private String shipper;

    @Builder
    public AuctionArticlesForAdminResponse(String code, String type, String name, Grade grade, int count, int bidPrice, LocalDateTime bidTime, String region, String shipper) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.grade = grade.getText();
        this.count = count;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        this.region = region;
        this.shipper = shipper;
    }
}
