package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.service.auction.dto.AddAuctionArticleDto;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.AuctionArticle;
import com.kkoch.admin.domain.auction.repository.AuctionArticleRepository;
import com.kkoch.admin.domain.auction.repository.AuctionRepository;
import com.kkoch.admin.domain.plant.Plant;
import com.kkoch.admin.domain.plant.repository.PlantRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.kkoch.admin.domain.auction.Status.READY;

@Transactional
class AuctionArticleServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuctionArticleService auctionArticleService;
    @Autowired
    private AuctionArticleRepository auctionArticleRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PlantRepository plantRepository;

    @DisplayName("[경매품 등록] 없는 경매일정에 경매품을 등록할 경우 에러가 발생한다.")
    @Test
    void addAuctionArticleAuctionError() {
        //given
        Plant savedPlant = plantRepository.save(Plant.builder().build());
        AddAuctionArticleDto dto = getAddAuctionArticleDto();

        //when
        //then
        Assertions.assertThatThrownBy(() -> auctionArticleService.addAuctionArticle(savedPlant.getId(), -1L, dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 경매 일정입니다.");
    }

    @DisplayName("[경매품 등록] 등록되지 않은 식물을 경매품에 등록할 경우 예외가 발생한다.")
    @Test
    void addAuctionArticlePlantError() {
        //given
        Admin admin = insertAdmin();
        Auction savedAuction = insertAuction(admin);

        AddAuctionArticleDto dto = getAddAuctionArticleDto();

        //when
        //then
        Assertions.assertThatThrownBy(() -> auctionArticleService.addAuctionArticle(-1L, savedAuction.getId(), dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 식물입니다.");
    }

    @DisplayName("[경매품 등록]")
    @Test
    void addAuctionArticle() {
        //given
        Plant savedPlant = plantRepository.save(Plant.builder().build());

        Admin admin = insertAdmin();
        Auction savedAuction = insertAuction(admin);

        AddAuctionArticleDto dto = getAddAuctionArticleDto();

        //when
        Long auctionArticleId = auctionArticleService.addAuctionArticle(savedPlant.getId(), savedAuction.getId(), dto);

        //then
        Optional<AuctionArticle> findAuctionArticle = auctionArticleRepository.findById(auctionArticleId);
        Assertions.assertThat(findAuctionArticle).isPresent();
    }

    private static AddAuctionArticleDto getAddAuctionArticleDto() {
        return AddAuctionArticleDto.builder()
                .grade(Grade.NONE)
                .count(10)
                .region("광주")
                .shipper("꽃파라")
                .startPrice(20000)
                .build();
    }

    private AuctionArticle insertAuctionArticle(Plant plant, Auction auction) {
        AuctionArticle auctionArticle = AuctionArticle.builder()
                .plant(plant)
                .auction(auction)
                .auctionNumber("00001")
                .grade(Grade.NONE)
                .count(10)
                .region("광주")
                .shipper("꽃파라")
                .startPrice(20000)
                .bidPrice(0)
                .bidTime(LocalDateTime.now())
                .build();
        return auctionArticleRepository.save(auctionArticle);
    }

    private Auction insertAuction(Admin admin) {
        Auction auction = Auction.builder()
                .code(1)
                .startTime(LocalDateTime.of(2023, 9, 20, 5, 0))
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