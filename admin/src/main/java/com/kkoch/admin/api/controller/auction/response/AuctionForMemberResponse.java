package com.kkoch.admin.api.controller.auction.response;

import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuctionForMemberResponse {

    private Long auctionId;
    private String title;
    private Status status;
    private int code;

    @Builder
    private AuctionForMemberResponse(Long auctionId, String title, Status status, int code) {
        this.auctionId = auctionId;
        this.title = title;
        this.status = status;
        this.code = code;
    }

    public static AuctionForMemberResponse of(Auction auction) {
        return AuctionForMemberResponse.builder()
                .auctionId(auction.getId())
                .title(auction.getTitle())
                .status(auction.getStatus())
                .code(auction.getCode())
                .build();
    }
}
