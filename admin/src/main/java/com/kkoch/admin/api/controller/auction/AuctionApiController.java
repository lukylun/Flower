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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin-service/auctions")
@Slf4j
public class AuctionApiController {

    private final AuctionService auctionService;
    private final AuctionQueryService auctionQueryService;

    @PostMapping
    public ApiResponse<AuctionTitleResponse> addAuction(
            @Valid @RequestBody AddAuctionRequest request,
            @Login LoginAdmin loginAdmin
    ) {
        log.info("<경매 일정 등록> Controller : 구분코드 = {}", request.getCode());
        timeValidation(request.getStartTime());

        AddAuctionDto dto = request.toAddAuctionDto();

        AuctionTitleResponse response = auctionService.addAuction(loginAdmin.getId(), dto);
        log.info("[경매 일정 등록 응답] 경매방 제목 = {}", response.getTitle());
        return ApiResponse.ok(response);
    }

    @GetMapping("/api")
    public ApiResponse<AuctionTitleResponse> getAuctionListForMember() {
        log.info("<OPEN 상태의 경매 일정 조회> Controller");
        AuctionTitleResponse openAuction = auctionQueryService.getOpenAuction();
        log.info("<OPEN 상태의 경매 일정 조회> 경매방 이름 = {}", openAuction.getTitle());
        return ApiResponse.ok(openAuction);
    }

    @PatchMapping("/{auctionId}/{status}")
    public ApiResponse<Long> setAuctionStatus(
            @PathVariable Long auctionId,
            @PathVariable Status status
    ) {
        log.info("<경매 일정 상태 변경> Controller. 변경될 상태 = {}", status);
        AuctionTitleResponse response = auctionService.setStatus(auctionId, status);

        log.debug("[경매 일정 상태 변경] 경매방 제목 = {}", response.getTitle());
        return ApiResponse.ok(auctionId);
    }

    @PatchMapping("/{auctionId}")
    public ApiResponse<AuctionTitleResponse> setAuction(
            @PathVariable Long auctionId,
            @Valid @RequestBody SetAuctionRequest request
    ) {
        log.info("<경매 일정 변경> Controller.");
        timeValidation(request.getStartTime());

        SetAuctionDto dto = request.toSetAuctionDto();

        AuctionTitleResponse response = auctionService.setAuction(auctionId, dto);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/{auctionId}")
    public ApiResponse<Long> removeAuction(@PathVariable Long auctionId) {
        log.info("<경매 일정 삭제> Controller.");
        Long removedAuctionId = auctionService.remove(auctionId);

        return ApiResponse.of(MOVED_PERMANENTLY, "경매 일정이 삭제되었습니다.", removedAuctionId);
    }

    private void timeValidation(LocalDateTime startTime) {
        if (!startTime.isAfter(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("경매 시간 입력 오류");
        }
    }
}
