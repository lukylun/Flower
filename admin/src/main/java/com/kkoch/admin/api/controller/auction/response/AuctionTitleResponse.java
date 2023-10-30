package com.kkoch.admin.api.controller.auction.response;

import com.kkoch.admin.domain.auction.Auction;
import lombok.Builder;
import lombok.Data;

@Data
public class AuctionTitleResponse {

    private Long auctionId;
    private String title;

    @Builder
    private AuctionTitleResponse(Long auctionId, String title) {
        this.auctionId = auctionId;
        this.title = title;
    }

    public static AuctionTitleResponse of(Auction auction) {
        return AuctionTitleResponse.builder()
                .auctionId(auction.getId())
                .title(auction.getTitle())
                .build();
    }
}
