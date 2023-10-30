package com.kkoch.admin.api.controller.auction;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.admin.LoginAdmin;
import com.kkoch.admin.api.controller.auction.request.AddAuctionRequest;
import com.kkoch.admin.api.controller.auction.request.SetAuctionRequest;
import com.kkoch.admin.api.controller.auction.response.AuctionResponse;
import com.kkoch.admin.api.controller.auction.response.AuctionTitleResponse;
import com.kkoch.admin.api.service.auction.AuctionQueryService;
import com.kkoch.admin.api.service.auction.AuctionService;
import com.kkoch.admin.api.service.auction.dto.AddAuctionDto;
import com.kkoch.admin.api.service.auction.dto.SetAuctionDto;
import com.kkoch.admin.domain.auction.Status;
import com.kkoch.admin.login.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin-service/intranet")
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionQueryService auctionQueryService;

    @GetMapping("/auctions")
    public String getAuctions(Model model) {
        log.info("<모든 경매 일정 조회> Controller");
        List<AuctionResponse> auctionSchedule = auctionQueryService.getAuctionSchedule();
        model.addAttribute("auctions",auctionSchedule);
        return "auction";
    }
}
