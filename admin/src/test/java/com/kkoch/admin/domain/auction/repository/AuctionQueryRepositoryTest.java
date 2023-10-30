package com.kkoch.admin.domain.auction.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.auction.response.AuctionResponse;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class AuctionQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AuctionQueryRepository auctionQueryRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuctionRepository auctionRepository;


    @DisplayName("[경매 일정 조회(Repository)] 경매 일정 전체 조회")
    @Test
    void findAllAuction() {
        //given
        Admin admin = insertAdmin();
        insertAuction(admin, LocalDateTime.of(2023, 9, 15, 5, 0),Status.READY);
        insertAuction(admin, LocalDateTime.of(2023, 9, 16, 5, 0), Status.OPEN);
        insertAuction(admin, LocalDateTime.of(2023, 9, 17, 5, 0),Status.READY);
        insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0),Status.OPEN);

        List<AuctionResponse> allAuction = auctionQueryRepository.findAllAuction();

        assertThat(allAuction).hasSize(4);
    }


    @DisplayName("[경매 일정 조회(Repository)] 오픈된 경매 일정 조회")
    @Test
    void findOpenAuction() {
        //given
        Admin admin = insertAdmin();
        insertAuction(admin, LocalDateTime.of(2023, 9, 15, 5, 0),Status.READY);
        Auction auction = insertAuction(admin, LocalDateTime.of(2023, 9, 16, 5, 0), Status.OPEN);
        insertAuction(admin, LocalDateTime.of(2023, 9, 17, 5, 0),Status.READY);
        insertAuction(admin, LocalDateTime.of(2023, 9, 20, 5, 0),Status.OPEN);

        Optional<Auction> openAuction = auctionQueryRepository.findOpenAuction();

        assertThat(openAuction).isPresent();
        assertThat(openAuction.get().getStartTime()).isEqualTo(auction.getStartTime());
    }

    private Auction insertAuction(Admin admin, LocalDateTime startTime, Status status) {
        Auction auction = Auction.builder()
                .code(1)
                .startTime(startTime)
                .active(true)
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