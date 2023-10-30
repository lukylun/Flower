package com.kkoch.admin.domain.auction.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesForAdminResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.api.controller.trade.response.SuccessfulBid;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticleSearchForAdminCond;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import com.kkoch.admin.domain.trade.Trade;
import com.kkoch.admin.domain.trade.repository.TradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kkoch.admin.domain.auction.Status.READY;
import static org.assertj.core.api.Assertions.*;

@Transactional
class AuctionArticleQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AuctionArticleQueryRepository auctionArticleQueryRepository;
    @Autowired
    private AuctionArticleRepository auctionArticleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuctionRepository auctionRepository;

    @DisplayName("[경매품 조회(Repository)] 경매용")
    @Test
    void getAuctionArticles() {
        Category code = insertCategory("절화");
        Category rose = insertCategory("장미");
        Category fuego = insertCategory("푸에고");
        Category victoria = insertCategory("빅토리아");

        Plant roseFuego = insertPlant(code, rose, fuego);
        Plant roseVictoria = insertPlant(code, rose, victoria);

        Admin admin = insertAdmin();
        Auction savedAuction1 = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));
        Auction savedAuction2 = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));

        insertAuctionArticle(roseFuego, savedAuction1, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseFuego, savedAuction2, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(10));
        insertAuctionArticle(roseFuego, savedAuction1, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseVictoria, savedAuction1, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseVictoria, savedAuction2, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(10));
        insertAuctionArticle(roseVictoria, savedAuction1, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));

        //when
        List<AuctionArticlesResponse> response = auctionArticleQueryRepository.getAuctionArticleList(savedAuction1.getId());

        //then
        assertThat(response).hasSize(4);
    }

    @DisplayName("[경매품 조회] 관리자용")
    @Test
    void getAuctionArticleForAdmin() {
        Category code = insertCategory("절화");
        Category rose = insertCategory("장미");
        Category fuego = insertCategory("푸에고");
        Category victoria = insertCategory("빅토리아");

        Plant roseFuego = insertPlant(code, rose, fuego);
        Plant roseVictoria = insertPlant(code, rose, victoria);

        Admin admin = insertAdmin();
        Auction savedAuction = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));

        insertAuctionArticle(roseFuego, savedAuction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(10));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseVictoria, savedAuction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(10));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));

        AuctionArticleSearchForAdminCond cond = AuctionArticleSearchForAdminCond.builder()
                .code("절화")
                .type("장미")
                .name(null)
                .endDateTime(LocalDateTime.of(2023, 9, 20, 5, 0).toLocalDate())
                .region("광주")
                .shipper("꽃파라")
                .build();

        //when
        List<AuctionArticlesForAdminResponse> responses = auctionArticleQueryRepository.getAuctionArticleListForAdmin(cond);

        //then
        assertThat(responses).hasSize(2);
    }

    @DisplayName("[경매품 조회] 기간 조회")
    @Test
    void getAuctionArticlePeriod() {
        Category code = insertCategory("절화");
        Category rose = insertCategory("장미");
        Category fuego = insertCategory("푸에고");
        Category victoria = insertCategory("빅토리아");

        Plant roseFuego = insertPlant(code, rose, fuego);
        Plant roseVictoria = insertPlant(code, rose, victoria);

        Admin admin = insertAdmin();
        Auction savedAuction = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));

        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 18, 5, 0));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 16, 5, 0));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 14, 5, 0));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 13, 5, 0));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 12, 5, 0));

        AuctionArticlePeriodSearchCond cond = AuctionArticlePeriodSearchCond.builder()
                .code("절화")
                .type("장미")
                .name(null)
                .startDateTime(LocalDateTime.of(2023, 9, 13, 5, 0).toLocalDate())
                .endDateTime(LocalDateTime.of(2023, 9, 20, 5, 0).toLocalDate())
                .region("광주")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 20);

        //when
        List<AuctionArticlePeriodSearchResponse> responses = auctionArticleQueryRepository.getAuctionArticleListForPeriodSearch(cond, pageRequest);

        //then
        assertThat(responses).hasSize(5);
    }

    @DisplayName("[낙찰내역 상세 조회]")
    @Test
    void findByTradeId() {
        //given
        Category code = insertCategory("절화");
        Category rose = insertCategory("장미");
        Category fuego = insertCategory("푸에고");

        Plant roseFuego = insertPlant(code, rose, fuego);
        Admin admin = insertAdmin();
        Auction auction = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));

        AuctionArticle auctionArticle1 = insertAuctionArticle(roseFuego, auction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        AuctionArticle auctionArticle2 = insertAuctionArticle(roseFuego, auction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        AuctionArticle auctionArticle3 = insertAuctionArticle(roseFuego, auction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));

        Trade trade = insertTrade(30000);

        auctionArticle1.updateTrade(trade);
        auctionArticle2.updateTrade(trade);
        auctionArticle3.updateTrade(trade);

        //when
        List<SuccessfulBid> responses = auctionArticleQueryRepository.findByTradeId(trade.getId());

        //then
        assertThat(responses).hasSize(3)
                .extracting("code", "type", "name")
                .containsExactlyInAnyOrder(
                        tuple(code.getName(), rose.getName(), fuego.getName()),
                        tuple(code.getName(), rose.getName(), fuego.getName()),
                        tuple(code.getName(), rose.getName(), fuego.getName())
                );
    }

    private Trade insertTrade(int totalPrice) {
        Trade trade = Trade.builder()
                .totalPrice(totalPrice)
                .tradeTime(LocalDateTime.now())
                .pickupStatus(false)
                .active(true)
                .memberKey("memberKey")
                .articles(new ArrayList<>())
                .build();
        return tradeRepository.save(trade);
    }

    private Category insertCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        return categoryRepository.save(category);
    }

    private Plant insertPlant(Category code, Category type, Category name) {
        return plantRepository.save(Plant.builder()
                .code(code)
                .type(type)
                .name(name)
                .build());
    }

    private Auction insertAuction(Admin admin, LocalDateTime startTime) {
        Auction auction = Auction.builder()
                .code(1)
                .startTime(startTime)
                .active(true)
                .status(READY)
                .admin(admin)
                .build();
        return auctionRepository.save(auction);
    }

    private Admin insertAdmin() {
        Admin admin = Admin.builder()
                .loginId("admin")
                .loginPw("admin123!")
                .name("관리자")
                .position("10")
                .tel("010-0000-0000")
                .active(true)
                .build();
        return adminRepository.save(admin);
    }

    private AuctionArticle insertAuctionArticle(Plant plant, Auction auction, String region, LocalDateTime bidTime) {
        AuctionArticle auctionArticle = AuctionArticle.builder()
                .plant(plant)
                .auction(auction)
                .auctionNumber("00001")
                .grade(Grade.NONE)
                .count(10)
                .region(region)
                .shipper("꽃파라")
                .startPrice(20000)
                .bidPrice(10000)
                .bidTime(bidTime)
                .build();
        return auctionArticleRepository.save(auctionArticle);
    }
}