package com.kkoch.admin.api.controller.trade;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.trade.request.AddTradeRequest;
import com.kkoch.admin.api.controller.trade.response.TradeDetailResponse;
import com.kkoch.admin.api.controller.trade.response.TradeResponse;
import com.kkoch.admin.api.service.trade.TradeQueryService;
import com.kkoch.admin.api.service.trade.TradeService;
import com.kkoch.admin.api.service.trade.dto.AddTradeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin-service/trades")
@Slf4j
public class TradeController {

    private final TradeService tradeService;
    private final TradeQueryService tradeQueryService;

    @PostMapping
    public ApiResponse<Long> addTrade(@Valid @RequestBody AddTradeRequest request) {
        log.info("<낙찰->내역 기록> TradeController 요청");

        AddTradeDto dto = request.toAddTradeDto();

        Long tradeId = tradeService.addTrade(dto, LocalDate.now().atStartOfDay());
        log.info("<낙찰->내역 기록> TradeController. tradeId={}", tradeId);
        return ApiResponse.ok(tradeId);
    }

    @GetMapping("/{memberKey}")
    public ApiResponse<Page<TradeResponse>> getMyTrades(
            @PathVariable String memberKey,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        log.info("<회원 낙찰 내역 불러오기> TradeController");
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<TradeResponse> responses = tradeQueryService.getMyTrades(memberKey, pageRequest);
        return ApiResponse.ok(responses);
    }

    @GetMapping("/{memberKey}/{tradeId}")
    public ApiResponse<TradeDetailResponse> getTrade(@PathVariable Long tradeId, @PathVariable String memberKey) {
        TradeDetailResponse response = tradeQueryService.getTrade(tradeId);
        log.info("<회원 낙찰 내역 상세조회> TradeController");
        return ApiResponse.ok(response);
    }

    @PatchMapping("/{tradeId}")
    public ApiResponse<Long> pickup(@PathVariable Long tradeId) {
        log.info("<거래내역 픽업> TradeController");
        Long pickupTradeId = tradeService.pickup(tradeId);
        return ApiResponse.ok(pickupTradeId);
    }

    @DeleteMapping("/{tradeId}")
    public ApiResponse<Long> removeTrade(@PathVariable Long tradeId) {
        log.info("<거래내역 삭제> TradeController");
        Long removedId = tradeService.remove(tradeId);
        return ApiResponse.of(MOVED_PERMANENTLY, "낙찰 내역이 삭제되었습니다.", removedId);
    }
}
