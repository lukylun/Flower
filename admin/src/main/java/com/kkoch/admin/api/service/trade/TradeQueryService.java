package com.kkoch.admin.api.service.trade;

import com.kkoch.admin.api.controller.trade.response.SuccessfulBid;
import com.kkoch.admin.api.controller.trade.response.TradeDetailResponse;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.domain.auction.repository.AuctionArticleQueryRepository;
import com.kkoch.admin.domain.trade.repository.TradeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TradeQueryService {

    private final TradeQueryRepository tradeQueryRepository;
    private final AuctionArticleQueryRepository auctionArticleQueryRepository;

    public Page<TradeResponse> getMyTrades(String memberKey, Pageable pageable) {
        List<TradeResponse> contents = auctionArticleQueryRepository.findByTradeIds(memberKey, pageable);
        long totalCount = auctionArticleQueryRepository.getTotalCountForTrade(memberKey);
        return new PageImpl<>(contents, pageable, totalCount);
    }

    public TradeDetailResponse getTrade(Long tradeId) {
        TradeDetailResponse response = tradeQueryRepository.findById(tradeId);
        List<SuccessfulBid> contents = auctionArticleQueryRepository.findByTradeId(tradeId);
        response.insertAuctionArticles(contents);
        return response;
    }
}
