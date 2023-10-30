package com.kkoch.admin.api.service.trade;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.service.trade.dto.AddTradeDto;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.AuctionArticleRepository;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.TradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class TradeServiceTest extends IntegrationTestSupport {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AuctionArticleRepository auctionArticleRepository;

    @DisplayName("경매 종료 후 낙찰 정보가 들어오면 낙찰 내역이 생성된다.")
    @Test
    void addTrade() {
        //given
        AuctionArticle auctionArticle1 = createAuctionArticle("00001");
        AuctionArticle auctionArticle2 = createAuctionArticle("00002");
        AuctionArticle auctionArticle3 = createAuctionArticle("00003");

        AddTradeDto dto1 = createAddTradeDto(auctionArticle1.getId(), 2500);
        AddTradeDto dto2 = createAddTradeDto(auctionArticle2.getId(), 3000);
        AddTradeDto dto3 = createAddTradeDto(auctionArticle3.getId(), 3500);

        //when
        Long tradeId1 = tradeService.addTrade(dto1, LocalDateTime.now());
        Long tradeId2 = tradeService.addTrade(dto2, LocalDateTime.now());
        Long tradeId3 = tradeService.addTrade(dto3, LocalDateTime.now());

        //then
        assertThat(tradeId1).isEqualTo(tradeId2).isEqualTo(tradeId3);
        Optional<Trade> findTrade = tradeRepository.findById(tradeId1);
        assertThat(findTrade).isPresent();
        assertThat(findTrade.get().getTotalPrice()).isEqualTo(9000);
        assertThat(findTrade.get().getArticles())
                .hasSize(3)
                .extracting("trade.id", "bidPrice")
                .containsExactlyInAnyOrder(
                        tuple(findTrade.get().getId(), 2500),
                        tuple(findTrade.get().getId(), 3000),
                        tuple(findTrade.get().getId(), 3500)
                );
    }

    @DisplayName("이미 픽업한 항목에 대해 다시 픽업을 시도하면 예외가 발생한다.")
    @Test
    void pickupWithDuplication() {
        //given
        Trade trade = createTrade(true);

        //when //then
        assertThatThrownBy(() -> tradeService.pickup(trade.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 픽업한 상품입니다.");
    }

    @DisplayName("경매품을 픽업받으면 픽업 상태가 변경이 된다.")
    @Test
    void pickup() {
        //given
        Trade trade = createTrade(false);

        //when
        Long tradeId = tradeService.pickup(trade.getId());

        //then
        Optional<Trade> findTrade = tradeRepository.findById(tradeId);
        assertThat(findTrade).isPresent();
        assertThat(findTrade.get().isPickupStatus()).isTrue();
    }

    @DisplayName("낙찰 내역을 삭제하면 활성화 상태가 변경이 된다.")
    @Test
    void remove() {
        //given
        Trade trade = createTrade(true);

        //when
        Long tradeId = tradeService.remove(trade.getId());

        //then
        Optional<Trade> findTrade = tradeRepository.findById(tradeId);
        assertThat(findTrade).isPresent();
        assertThat(findTrade.get().isActive()).isFalse();
    }

    private AuctionArticle createAuctionArticle(String auctionNumber) {
        AuctionArticle auctionArticle = AuctionArticle.builder()
                .auctionNumber(auctionNumber)
                .grade(Grade.NONE)
                .count(10)
                .bidPrice(0)
                .bidTime(LocalDateTime.now())
                .region("광주")
                .shipper("김싸피")
                .startPrice(5_000)
                .build();
        return auctionArticleRepository.save(auctionArticle);
    }

    private AddTradeDto createAddTradeDto(Long auctionArticleId, int bidPrice) {
        return AddTradeDto.builder()
                .auctionArticleId(auctionArticleId)
                .memberKey("memberKey")
                .price(bidPrice)
                .build();
    }

    private Trade createTrade(boolean pickupStatus) {
        Trade trade = Trade.builder()
                .totalPrice(10000)
                .tradeTime(LocalDate.of(2023, 7, 10).atStartOfDay())
                .pickupStatus(pickupStatus)
                .active(true)
                .memberKey("memberKey")
                .articles(new ArrayList<>())
                .build();
        return tradeRepository.save(trade);
    }
}