package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesForAdminResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.AuctionArticleRepository;
import com.kkoch.admin.domain.auction.repository.AuctionRepository;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticleSearchForAdminCond;
import com.kkoch.admin.domain.plant.Category;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.repository.CategoryRepository;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.kkoch.admin.domain.auction.Status.READY;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class AuctionArticleQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuctionArticleQueryService auctionArticleQueryService;
    @Autowired
    private AuctionArticleRepository auctionArticleRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("[경매품 전체 조회] (경매용)")
    @Test
    void getAuctionArticleListForAuction() {
        //given
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

        AuctionArticleSearchForAdminCond cond = AuctionArticleSearchForAdminCond.builder()
                .code("절화")
                .type("장미")
                .name("푸에고")
                .endDateTime(LocalDateTime.of(2023, 9, 20, 5, 0).toLocalDate())
                .region("광주")
                .shipper("꽃파라")
                .build();

        //when
        List<AuctionArticlesResponse> response = auctionArticleQueryService.getAuctionArticleList(savedAuction1.getId());

        //then
        assertThat(response).hasSize(4);
    }

    @DisplayName("[경매품 전체 조회] (관리자)")
    @Test
    void getAllAuctionArticleListForAdmin() {
        //given
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
                .name("푸에고")
                .endDateTime(LocalDateTime.of(2023, 9, 20, 5, 0).toLocalDate())
                .region("광주")
                .shipper("꽃파라")
                .build();

        //when
        List<AuctionArticlesForAdminResponse> response = auctionArticleQueryService.getAuctionArticleListForAdmin(cond);

        //then
        assertThat(response).hasSize(1);
    }

    @DisplayName("[실시간 거래실적 조회] 기간 검색 조건 추가")
    @Test
    void getAuctionArticleListForPeriod() {
        //given
        Category code = insertCategory("절화");
        Category rose = insertCategory("장미");
        Category fuego = insertCategory("푸에고");
        Category victoria = insertCategory("빅토리아");

        Plant roseFuego = insertPlant(code, rose, fuego);
        Plant roseVictoria = insertPlant(code, rose, victoria);

        Admin admin = insertAdmin();
        Auction savedAuction = insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0));

        insertAuctionArticle(roseFuego, savedAuction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(2));
        insertAuctionArticle(roseFuego, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(3));
        insertAuctionArticle(roseVictoria, savedAuction, "서울", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(4));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(5));
        insertAuctionArticle(roseVictoria, savedAuction, "광주", LocalDateTime.of(2023, 9, 20, 5, 0).minusDays(6));

        AuctionArticlePeriodSearchCond cond = AuctionArticlePeriodSearchCond.builder()
                .code("절화")
                .type("장미")
                .name("")
                .endDateTime(LocalDateTime.of(2023, 9, 20, 5, 0).toLocalDate())
                .startDateTime(LocalDateTime.of(2023, 9, 15, 5, 0).toLocalDate())
                .region("")
                .build();
        PageRequest pageRequest = PageRequest.of(0, 20);

        //when
        Page<AuctionArticlePeriodSearchResponse> result = auctionArticleQueryService.getAuctionArticlePeriodSearch(cond, pageRequest);

        //then
        List<AuctionArticlePeriodSearchResponse> responses = result.getContent();
        assertThat(responses).hasSize(5);
    }

    private Plant insertPlant(Category code, Category type, Category name) {
        return plantRepository.save(Plant.builder()
                .code(code)
                .type(type)
                .name(name)
                .build());
    }

    private Category insertCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        return categoryRepository.save(category);
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
}