package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.auction.response.AuctionResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionTitleResponse;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.Status;
import com.kkoch.admin.domain.auction.repository.AuctionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.kkoch.admin.domain.auction.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class AuctionQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuctionQueryService auctionQueryService;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("[경매 일정 조회] 진행중인 경매 없음")
    @Test
    void getAuctionNotExistOpen() {
        //given
        Admin admin = insertAdmin();
        Auction auction1 = insertAuction(admin, false, READY, LocalDateTime.of(2023, 9, 15, 5, 0));
        Auction auction2 = insertAuction(admin, true, CLOSE, LocalDateTime.of(2023, 9, 16, 5, 0));
        Auction auction3 = insertAuction(admin, true, READY, LocalDateTime.of(2023, 9, 17, 5, 0));
        Auction auction4 = insertAuction(admin, true, READY, LocalDateTime.of(2023, 9, 18, 5, 0));
        Auction auction5 = insertAuction(admin, true, READY, LocalDateTime.of(2023, 9, 19, 5, 0));

        //when
        //then
        assertThatThrownBy(() -> auctionQueryService.getOpenAuction())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("진행중인 경매가 없습니다.");

    }

    @DisplayName("[경매 일정 조회] 진행중인 경매 조회")
    @Test
    void getOpenAuction() {
        //given
        Admin admin = insertAdmin();
        Auction auction1 = insertAuction(admin, false, READY, LocalDateTime.of(2023, 9, 15, 5, 0));
        Auction auction2 = insertAuction(admin, true, CLOSE, LocalDateTime.of(2023, 9, 16, 5, 0));
        Auction auction3 = insertAuction(admin, true, OPEN, LocalDateTime.of(2023, 9, 17, 5, 0));
        Auction auction4 = insertAuction(admin, true, READY, LocalDateTime.of(2023, 9, 18, 5, 0));
        Auction auction5 = insertAuction(admin, true, READY, LocalDateTime.of(2023, 9, 19, 5, 0));

        //when
        AuctionTitleResponse openAuction = auctionQueryService.getOpenAuction();
        //then
        assertThat(openAuction.getTitle()).isEqualTo("23. 9. 17. 오전 5:00 절화 진행 중");
    }

    @DisplayName("[경매 일정 조회] 앞으로 열릴 경매 일정 조회")
    @Test
    void getAuctionList() {
        //given
        Admin admin = insertAdmin();
        Auction auction1 = insertAuction(admin, false, READY);
        Auction auction2 = insertAuction(admin, true, CLOSE);
        Auction auction3 = insertAuction(admin, true, OPEN);
        Auction auction4 = insertAuction(admin, true, READY);
        Auction auction5 = insertAuction(admin, true, READY);

        //when
        List<AuctionResponse> auctionSchedule = auctionQueryService.getAuctionSchedule();

        //then
        assertThat(auctionSchedule).hasSize(3);
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

    private Auction insertAuction(Admin admin, boolean active, Status status) {
        Auction auction = Auction.builder()
                .code(1)
                .startTime(LocalDateTime.of(2023, 9, 20, 5, 0))
                .active(active)
                .status(status)
                .admin(admin)
                .build();
        return auctionRepository.save(auction);
    }

    private Auction insertAuction(Admin admin, boolean active, Status status, LocalDateTime startTime) {
        Auction auction = Auction.builder()
                .code(1)
                .startTime(startTime)
                .active(active)
                .status(status)
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