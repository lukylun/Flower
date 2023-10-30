package com.kkoch.auction.api.controller;

import com.kkoch.auction.api.ApiResponse;
import com.kkoch.auction.api.controller.request.AddTradeRequest;
import com.kkoch.auction.api.controller.request.EventParticipant;
import com.kkoch.auction.api.controller.response.EventResultResponse;
import com.kkoch.auction.api.service.RedisService;
import com.kkoch.auction.client.AdminServiceClient;
import com.kkoch.auction.api.service.dto.AuctionArticlesResponse;
import com.kkoch.auction.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auction-service/auctions")
@Slf4j
public class EventController {

    private final RedisService redisService;
    private final AdminServiceClient adminServiceClient;
    private final UserServiceClient userServiceClient;

    @PostMapping("/{auctionId}")
    public ApiResponse<AuctionArticlesResponse> getFirstArticle(@PathVariable Long auctionId) throws IOException {
//        List<AuctionArticlesResponse> articles = adminServiceClient.getAuctionArticlesForAuction(auctionId).getData();
        AuctionArticlesResponse article1 = getArticle(1L);
        AuctionArticlesResponse article2 = getArticle(2L);
        AuctionArticlesResponse article3 = getArticle(3L);
        AuctionArticlesResponse article4 = getArticle(4L);
        AuctionArticlesResponse article5 = getArticle(5L);
        AuctionArticlesResponse article6 = getArticle(6L);
        AuctionArticlesResponse article7 = getArticle(7L);
        List<AuctionArticlesResponse> articles = List.of(article1, article2, article3, article4, article5, article6, article7);
        redisService.insertList(articles);
        AuctionArticlesResponse nextArticle = redisService.getNextArticle();
        return ApiResponse.ok(nextArticle);
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<AuctionArticlesResponse> getNextArticle(@PathVariable Long auctionId) throws IOException {
//        List<AuctionArticlesResponse> articles = adminServiceClient.getAuctionArticlesForAuction(auctionId).getData();
        AuctionArticlesResponse article1 = getArticle(1L);
        AuctionArticlesResponse article2 = getArticle(2L);
        AuctionArticlesResponse article3 = getArticle(3L);
        AuctionArticlesResponse article4 = getArticle(4L);
        AuctionArticlesResponse article5 = getArticle(5L);
        AuctionArticlesResponse article6 = getArticle(6L);
        AuctionArticlesResponse article7 = getArticle(7L);
//        List<AuctionArticlesResponse> articles = List.of(article1, article2, article3, article4, article5, article6, article7);
//        redisService.insertList(articles);
        AuctionArticlesResponse nextArticle = redisService.getNextArticle();
        return ApiResponse.ok(nextArticle);
    }

    //지우기
    private static AuctionArticlesResponse getArticle(long auctionArticleId) {
        return AuctionArticlesResponse.builder()
                .auctionArticleId(auctionArticleId)
                .auctionNumber("1-0000" + auctionArticleId)
                .plantId(1L)
                .code("절화")
                .type("장미")
                .name("푸에고")
                .count((int) (9 + auctionArticleId))
                .startPrice((int) (auctionArticleId * 1000))
                .grade("SUPER")
                .region("광주")
                .shipper("김수진")
                .build();
    }

    @PostMapping("/participant")
    public ApiResponse<EventResultResponse> joinEvent(@RequestBody EventParticipant participant) {
        int memberNumber = redisService.getNumber(participant.getMemberKey());
        log.info("{}의 회원번호 = {}", participant.getMemberKey(), memberNumber);
        log.info("debug request={}", participant);
        String auctionArticleId = participant.getAuctionArticleId();
        boolean isWinner = redisService.joinEvent(participant);
        if (isWinner) {
            AddTradeRequest addTradeRequest = generateAddTradeRequest(participant);
            ApiResponse<Long> addTradeResponse = adminServiceClient.addTrade(addTradeRequest);
            String winnerPrice = redisService.getWinnerPrice(getPriceKey(auctionArticleId), participant.getMemberKey());
            //예약 삭제
            if (participant.getReservationId() > 0) {
                userServiceClient.removeReservation(participant.getReservationId());
            }
            return ApiResponse.ok(EventResultResponse.success(participant, String.valueOf(memberNumber), winnerPrice));
        }
        String winnerKey = redisService.getWinnerKey(auctionArticleId);
        String winnerNumber = String.valueOf(redisService.getNumber(winnerKey));
        String winnerPrice = redisService.getWinnerPrice(getPriceKey(auctionArticleId), winnerKey);
        return ApiResponse.ok(EventResultResponse.fail(participant, winnerNumber, Integer.parseInt(winnerPrice)));
    }

    private static String getPriceKey(String auctionArticleId) {
        return "price-" + auctionArticleId;
    }

    private static AddTradeRequest generateAddTradeRequest(EventParticipant participant) {
        return AddTradeRequest.builder()
                .memberKey(participant.getMemberKey())
                .price(participant.getPrice())
                .auctionArticleId(participant.getAuctionArticleId())
                .build();
    }
}
