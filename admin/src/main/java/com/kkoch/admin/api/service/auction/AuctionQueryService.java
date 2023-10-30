package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.api.controller.auction.response.AuctionResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionTitleResponse;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.repository.AuctionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuctionQueryService {

    private final AuctionQueryRepository auctionQueryRepository;

    public List<AuctionResponse> getAuctionSchedule() {
        return auctionQueryRepository.findAllAuction();
    }

    public AuctionTitleResponse getOpenAuction() {
        Auction auction = auctionQueryRepository.findOpenAuction()
                .orElseThrow(() -> new NoSuchElementException("진행중인 경매가 없습니다."));
        return AuctionTitleResponse.of(auction);
    }
}
