package com.kkoch.auction.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationForAuctionResponse {

    private String memberKey;
    private Long reservationId;
    private int price;

    public ReservationForAuctionResponse(String memberKey, Long reservationId, int price) {
        this.memberKey = memberKey;
        this.reservationId = reservationId;
        this.price = price;
    }

    public String toJson() {
        return String.format("{" +
            "\"memberKey\":\"%s\"," +
            "\"reservationId\":%d," +
            "\"price\":%d" +
            "}", this.memberKey, this.reservationId, this.price);
    }
}

