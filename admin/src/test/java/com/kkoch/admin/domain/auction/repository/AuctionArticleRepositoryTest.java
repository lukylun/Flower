package com.kkoch.admin.domain.auction.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.domain.Grade;
import com.kkoch.admin.domain.auction.AuctionArticle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
class AuctionArticleRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AuctionArticleRepository auctionArticleRepository;

    @DisplayName("여러 경매품 PK로 해당하는 경매품들을 조회할 수 있다.")
    @Test
    void findByIdIn() {
        //given
        AuctionArticle auctionArticle1 = createAuctionArticle("00001");
        AuctionArticle auctionArticle2 = createAuctionArticle("00002");
        AuctionArticle auctionArticle3 = createAuctionArticle("00003");

        List<Long> ids = List.of(auctionArticle1.getId(), auctionArticle2.getId(), auctionArticle3.getId());

        //when
        List<AuctionArticle> auctionArticles = auctionArticleRepository.findByIdIn(ids);

        //then
        assertThat(auctionArticles).hasSize(3);
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
}