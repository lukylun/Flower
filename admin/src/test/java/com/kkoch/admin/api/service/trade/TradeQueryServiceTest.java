package com.kkoch.admin.api.service.trade;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.trade.response.TradeDetailResponse;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.AuctionArticleRepository;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.TradeRepository;
import com.kkoch.admin.domain.trade.repository.dto.TradeSearchCond;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
class TradeQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private TradeQueryService tradeQueryService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AuctionArticleRepository auctionArticleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PlantRepository plantRepository;

    @DisplayName("회원은 본인의 낙찰 내역을 기간으로 조회를 할 수 있다.")
    @Test
    void getMyTrades() {
        //given
        LocalDate currentDate = LocalDate.of(2023, 7, 10);

        Trade trade1 = createTrade(1000, LocalDateTime.of(2023, 7, 3, 23, 59, 59), true);
        Trade trade2 = createTrade(2000, LocalDateTime.of(2023, 7, 4, 0, 0, 0), true);
        Trade trade3 = createTrade(3000, LocalDateTime.of(2023, 7, 7, 0, 0, 0), false);
        Trade trade4 = createTrade(4000, LocalDateTime.of(2023, 7, 7, 0, 0, 0), true);
        Trade trade5 = createTrade(5000, LocalDateTime.of(2023, 7, 10, 23, 59, 59), true);

        createAuctionArticle("00001", trade2, null);
        createAuctionArticle("00002", trade2, null);
        createAuctionArticle("00003", trade2, null);
        createAuctionArticle("00004", trade5, null);
        createAuctionArticle("00005", trade5, null);

        PageRequest pageRequest = PageRequest.of(0, 20);

        //when
        Page<TradeResponse> result = tradeQueryService.getMyTrades("memberKey", pageRequest);

        //then
        List<TradeResponse> responses = result.getContent();
        assertThat(responses).hasSize(3)
                .extracting("totalPrice", "count")
                .containsExactlyInAnyOrder(
                        tuple(2000, 3),
                        tuple(4000, 0),
                        tuple(5000, 2)
                );
    }

    @DisplayName("회원은 본인의 낙찰 내역을 상세 조회를 할 수 있다.")
    @Test
    void getTrade() {
        //given
        Category code = createCategory("절화", null);
        Category name = createCategory("장미(스탠다드)", code);
        Category type = createCategory("하젤", name);

        Plant plant = createPlant(code, name, type);

        Trade trade = createTrade(3000, LocalDate.of(2023, 7, 10).atStartOfDay(), true);

        createAuctionArticle("00001", trade, plant);
        createAuctionArticle("00002", trade, plant);
        createAuctionArticle("00003", trade, plant);

        //when
        TradeDetailResponse response = tradeQueryService.getTrade(trade.getId());

        //then
        assertThat(response.getTotalPrice()).isEqualTo(3000);
        assertThat(response.getSuccessfulBid()).hasSize(3)
                .extracting("code", "name", "type")
                .containsExactlyInAnyOrder(
                        tuple(code.getName(), name.getName(), type.getName()),
                        tuple(code.getName(), name.getName(), type.getName()),
                        tuple(code.getName(), name.getName(), type.getName())
                );
    }

    private AuctionArticle createAuctionArticle(String auctionNumber, Trade trade, Plant plant) {
        AuctionArticle auctionArticle = AuctionArticle.builder()
                .plant(plant)
                .trade(trade)
                .auctionNumber(auctionNumber)
                .grade(Grade.NONE)
                .count(10)
                .bidPrice(1000)
                .bidTime(LocalDateTime.now())
                .region("광주")
                .shipper("김싸피")
                .startPrice(5000)
                .build();
        return auctionArticleRepository.save(auctionArticle);
    }

    private Trade createTrade(int totalPrice, LocalDateTime tradeDate, boolean active) {
        Trade trade = Trade.builder()
                .memberKey("memberKey")
                .totalPrice(totalPrice)
                .tradeTime(tradeDate)
                .pickupStatus(false)
                .articles(new ArrayList<>())
                .active(active)
                .build();
        return tradeRepository.save(trade);
    }

    private Category createCategory(String name, Category parent) {
        Category category = Category.builder()
                .name(name)
                .active(true)
                .parent(parent)
                .build();
        return categoryRepository.save(category);
    }

    private Plant createPlant(Category code, Category name, Category type) {
        Plant plant = Plant.builder()
                .active(true)
                .code(code)
                .name(name)
                .type(type)
                .build();
        return plantRepository.save(plant);
    }
}