package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesForAdminResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.domain.auction.repository.AuctionArticleQueryRepository;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticleSearchForAdminCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class AuctionArticleQueryService {

    private final AuctionArticleQueryRepository auctionArticleQueryRepository;

    public Page<AuctionArticlePeriodSearchResponse> getAuctionArticlePeriodSearch(AuctionArticlePeriodSearchCond cond, Pageable pageable) {
        log.info("<경매품 목록조회(기간별 조회)> AuctionArticleQueryService");
        List<AuctionArticlePeriodSearchResponse> responses = auctionArticleQueryRepository.getAuctionArticleListForPeriodSearch(cond, pageable);
        int totalCount = auctionArticleQueryRepository.getTotalCountForPeriod(cond);
        return new PageImpl<>(responses, pageable, totalCount);
    }

    public List<AuctionArticlesResponse> getAuctionArticleList(Long auctionId) {
        log.info("<경매품 목록조회(경매시 목록 출력용)> AuctionArticleQueryService");
        return auctionArticleQueryRepository.getAuctionArticleList(auctionId);
    }

    public List<AuctionArticlesForAdminResponse> getAuctionArticleListForAdmin(AuctionArticleSearchForAdminCond cond) {
        log.info("<경매품 목록조회(관계자용 전체 조회)> AuctionArticleQueryService");
        return auctionArticleQueryRepository.getAuctionArticleListForAdmin(cond);
    }
}
