package com.kkoch.auction.api.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class EventParticipant {

    @NotNull
    private String memberKey;
    @NotNull
    private String auctionArticleId;
    @NotNull
    private Integer price;
    @NotNull
    private Long reservationId;

    public EventParticipant(String memberKey, String auctionArticleId, Integer price, Long reservationId) {
        this.memberKey = memberKey;
        this.auctionArticleId = auctionArticleId;
        this.price = price;
        this.reservationId = reservationId;
    }
}
