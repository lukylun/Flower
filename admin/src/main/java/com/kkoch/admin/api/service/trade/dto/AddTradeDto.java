package com.kkoch.admin.api.service.trade.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AddTradeDto {

    private String memberKey;
    private Long auctionArticleId;
    private int price;

    @Builder
    private AddTradeDto(String memberKey, Long auctionArticleId, int price) {
        this.memberKey = memberKey;
        this.auctionArticleId = auctionArticleId;
        this.price = price;
    }
}
