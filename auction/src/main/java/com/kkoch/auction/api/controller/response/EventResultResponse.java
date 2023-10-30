package com.kkoch.auction.api.controller.response;

import com.kkoch.auction.api.controller.request.EventParticipant;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventResultResponse {

    private String memberKey;
    private Long auctionArticleId;
    private int bidPrice;
    private String winnerNumber;
    private String message;

    @Builder
    private EventResultResponse(String memberKey, String auctionArticleId, int bidPrice, String winnerNumber, String message) {
        this.memberKey = memberKey;
        this.auctionArticleId = Long.valueOf(auctionArticleId);
        this.bidPrice = bidPrice;
        this.winnerNumber = winnerNumber;
        this.message = message;
    }

    public static EventResultResponse fail(EventParticipant participant, String winnerNumber, int winnerPrice) {
        return EventResultResponse.builder()
                .auctionArticleId(participant.getAuctionArticleId())
                .memberKey(participant.getMemberKey())
                .bidPrice(winnerPrice)
                .winnerNumber(winnerNumber)
                .message("이미 낙찰되었습니다.")
                .build();
    }

    public static EventResultResponse success(EventParticipant participant, String winnerNumber, String winnerPrice) {
        return EventResultResponse.builder()
                .auctionArticleId(participant.getAuctionArticleId())
                .memberKey(participant.getMemberKey())
                .bidPrice(Integer.parseInt(winnerPrice))
                .winnerNumber(winnerNumber)
                .message("낙찰 성공")
                .build();
    }
}
