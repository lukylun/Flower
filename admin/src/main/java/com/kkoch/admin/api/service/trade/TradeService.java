package com.kkoch.admin.api.service.trade;

import com.kkoch.admin.api.service.trade.dto.AddTradeDto;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.AuctionArticleRepository;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;
    private final AuctionArticleRepository auctionArticleRepository;

    public Long addTrade(AddTradeDto dto, LocalDateTime tradeDate) {
        log.info("<낙찰->내역 기록> TradeService. 낙찰 날짜 : {}", tradeDate);
        Trade currnetTrade = null;

        Optional<Trade> findTrade = tradeRepository.findByMemberKey(dto.getMemberKey(), tradeDate);

        AuctionArticle auctionArticle = auctionArticleRepository.findById(dto.getAuctionArticleId())
                .orElseThrow(NoSuchElementException::new);

        if (findTrade.isEmpty()) {
            log.info("<낙찰->내역 기록> TradeService. 낙찰내역 생성");
            currnetTrade = tradeRepository.save(createTradeEntity(dto, tradeDate));
        }

        if (findTrade.isPresent()) {
            log.info("<낙찰->내역 기록> TradeService. 기존 낙찰내역에 추가");
            currnetTrade = findTrade.get();
            currnetTrade.setTotalPrice(dto.getPrice() * auctionArticle.getCount());
            log.info("<낙찰->내역 기록> TradeService. 총 거래 가격={}", currnetTrade.getTotalPrice());
        }
        auctionArticle.bid(dto.getPrice(), tradeDate);
        auctionArticle.updateTrade(currnetTrade);

        return currnetTrade.getId();
    }

    public Long pickup(Long tradeId) {
        Trade trade = getTradeEntity(tradeId);
        trade.pickup();
        return trade.getId();
    }

    public Long remove(Long tradeId) {
        Trade trade = getTradeEntity(tradeId);
        trade.remove();
        return trade.getId();
    }

    private static Trade createTradeEntity(AddTradeDto dto, LocalDateTime tradeDate) {
        return Trade.builder()
                .totalPrice(dto.getPrice())
                .tradeTime(tradeDate)
                .pickupStatus(false)
                .active(true)
                .memberKey(dto.getMemberKey())
                .articles(new ArrayList<>())
                .build();
    }

    private Trade getTradeEntity(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(NoSuchElementException::new);
    }
}
