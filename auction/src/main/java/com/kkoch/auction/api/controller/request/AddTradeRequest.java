package com.kkoch.auction.api.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddTradeRequest {

    private String memberKey;
    private Long auctionArticleId;
    private Integer price;

    @Builder
    private AddTradeRequest(String memberKey, String auctionArticleId, Integer price) {
        this.memberKey = memberKey;
        this.auctionArticleId = Long.valueOf(auctionArticleId);
        this.price = price;
    }
}
