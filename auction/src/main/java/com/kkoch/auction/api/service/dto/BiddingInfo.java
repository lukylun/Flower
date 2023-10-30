package com.kkoch.auction.api.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BiddingInfo {

    private String memberKey;
    private int price;

    @Builder
    public BiddingInfo(String memberKey, int price) {
        this.memberKey = memberKey;
        this.price = price;
    }
}
