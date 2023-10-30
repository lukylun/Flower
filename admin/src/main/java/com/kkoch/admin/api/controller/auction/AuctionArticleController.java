package com.kkoch.admin.api.controller.auction;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.auction.request.AddAuctionArticleRequest;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlePeriodSearchResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesForAdminResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionArticlesResponse;
import com.kkoch.admin.api.service.auction.AuctionArticleQueryService;
import com.kkoch.admin.api.service.auction.AuctionArticleService;
import com.kkoch.admin.api.service.auction.dto.AddAuctionArticleDto;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticlePeriodSearchCond;
import com.kkoch.admin.domain.auction.repository.dto.AuctionArticleSearchForAdminCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin-service/intranet/articles")
@Slf4j
public class AuctionArticleController {

    private final AuctionArticleQueryService auctionArticleQueryService;

    @GetMapping
    public String getAuctionArticlesForAdmin(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateTime,
            @RequestParam(defaultValue = "절화") String code,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String region,
            @RequestParam(defaultValue = "") String shipper
    ) {
        log.info("<경매품 목록조회(관계자용 전체 조회)> AuctionArticleController");
        AuctionArticleSearchForAdminCond cond = AuctionArticleSearchForAdminCond.of(endDateTime, code, type, name, region, shipper);

        List<AuctionArticlesForAdminResponse> responses = auctionArticleQueryService.getAuctionArticleListForAdmin(cond);
        return "article";
    }

    @GetMapping("/{auctionId}")
    public String getAuctionArticlesByAuctionId(@PathVariable Long auctionId, Model model) {
        log.info("<경매 일정 당 경매품 목록조회)> AuctionArticleController");
        List<AuctionArticlesResponse> response = auctionArticleQueryService.getAuctionArticleList(auctionId);
        model.addAttribute("articles", response);
        model.addAttribute("auctionId",auctionId);
        return "article";
    }
}
