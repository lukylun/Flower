package com.kkoch.auction.client;

import com.kkoch.auction.api.ApiResponse;
import com.kkoch.auction.api.controller.request.AddTradeRequest;
import com.kkoch.auction.api.service.dto.AuctionArticlesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "admin-service")
public interface AdminServiceClient {

    @GetMapping("/admin-service/auction-articles/{auctionId}")
    ApiResponse<List<AuctionArticlesResponse>> getAuctionArticlesForAuction(@PathVariable Long auctionId);

    @PostMapping("/admin-service/trades")
    ApiResponse<Long> addTrade(@RequestBody AddTradeRequest request);

    @PatchMapping("/admin-service/auctions/{auctionId}/{status}")
    ApiResponse<Long> setAuctionStatus(@PathVariable Long auctionId, @PathVariable String status);
}
