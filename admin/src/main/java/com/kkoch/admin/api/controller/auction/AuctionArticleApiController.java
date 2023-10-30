package com.kkoch.admin.api.controller.auction;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.auction.request.AddAuctionArticleRequest;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.api.service.auction.AuctionArticleQueryService;
import com.kkoch.admin.api.service.auction.AuctionArticleService;
import com.kkoch.admin.api.service.auction.dto.AddAuctionArticleDto;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin-service/auction-articles")
@Slf4j
public class AuctionArticleApiController {

    private final AuctionArticleService auctionArticleService;
    private final AuctionArticleQueryService auctionArticleQueryService;

    @PostMapping
    public ApiResponse<Long> addAuctionArticle(
            @Valid @RequestBody AddAuctionArticleRequest request
    ) {
        log.info("<경매품 등록> Controller : request = {}", request);
        AddAuctionArticleDto dto = request.toAddAuctionArticleDto();
        Long savedAuctionArticleId = auctionArticleService.addAuctionArticle(request.getPlantId(), request.getAuctionId(), dto);
        log.info("<경매품 등록> 응답 : 경매품 상장번호 = {}", savedAuctionArticleId);
        return ApiResponse.ok(savedAuctionArticleId);
    }

    @GetMapping("/api")
    public ApiResponse<Page<AuctionArticlePeriodSearchResponse>> getAuctionArticlesPeriod(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateTime,
            @RequestParam(defaultValue = "절화") String code,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String region,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        log.info("<경매품 목록조회(기간별 조회)> AuctionArticleApiController");
        AuctionArticlePeriodSearchCond cond = AuctionArticlePeriodSearchCond.of(startDateTime, endDateTime, code, type, name, region);
        PageRequest pageRequest = PageRequest.of(page, 15);

        Page<AuctionArticlePeriodSearchResponse> responses = auctionArticleQueryService.getAuctionArticlePeriodSearch(cond, pageRequest);
        return ApiResponse.ok(responses);
    }

    @GetMapping("/{auctionId}")
    public ApiResponse<List<AuctionArticlesResponse>> getAuctionArticlesForAuction(
            @PathVariable Long auctionId) {
        log.info("<경매품 목록조회(경매시 목록 출력용)> AuctionArticleApiController");
        List<AuctionArticlesResponse> response = auctionArticleQueryService.getAuctionArticleList(auctionId);
        return ApiResponse.ok(response);
    }

//    @GetMapping
//    public ApiResponse<List<AuctionArticlesForAdminResponse>> getAuctionArticlesForAdmin(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateTime,
//            @RequestParam(defaultValue = "절화") String code,
//            @RequestParam(defaultValue = "") String type,
//            @RequestParam(defaultValue = "") String name,
//            @RequestParam(defaultValue = "") String region,
//            @RequestParam(defaultValue = "") String shipper
//    ) {
//        log.info("<경매품 목록조회(관계자용 전체 조회)> AuctionArticleController");
//        AuctionArticleSearchForAdminCond cond = AuctionArticleSearchForAdminCond.of(endDateTime, code, type, name, region, shipper);
//
//        List<AuctionArticlesForAdminResponse> responses = auctionArticleQueryService.getAuctionArticleListForAdmin(cond);
//        return ApiResponse.ok(responses);
//    }
}
