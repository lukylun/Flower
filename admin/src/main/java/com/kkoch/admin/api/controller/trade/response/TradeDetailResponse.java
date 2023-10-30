package com.kkoch.admin.api.controller.trade.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class TradeDetailResponse {

    private int totalPrice;
    private String tradeTime;
    private boolean pickupStatus;
    private List<SuccessfulBid> successfulBid;

    @Builder
    public TradeDetailResponse(int totalPrice, LocalDateTime tradeTime, boolean pickupStatus) {
        this.totalPrice = totalPrice;
        this.tradeTime = tradeTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
        this.pickupStatus = pickupStatus;
    }

    public void insertAuctionArticles(List<SuccessfulBid> successfulBid) {
        this.successfulBid = successfulBid;
    }
}
