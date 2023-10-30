package com.kkoch.user.api.controller.pointhistory;

import com.kkoch.user.api.controller.ApiResponse;
import com.kkoch.user.api.controller.pointhistory.request.AddPointHistoryRequest;
import com.kkoch.user.api.controller.pointhistory.response.AddPointHistoryResponse;
import com.kkoch.user.api.controller.pointhistory.response.PointHistoryResponse;
import com.kkoch.user.api.service.pointhistory.PointHistoryQueryService;
import com.kkoch.user.api.service.pointhistory.PointHistoryService;
import com.kkoch.user.api.service.pointhistory.dto.AddPointHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 포인트 내역 API 컨트롤러
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/{memberKey}/points")
@Slf4j
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;
    private final PointHistoryQueryService pointHistoryQueryService;

    /**
     * 포인트 충전 API
     *
     * @param memberKey 회원 고유키
     * @param request 포인트 충전 요청 객체
     * @return 등록된 포인트 내역
     */
    @PostMapping("/charge")
    public ApiResponse<AddPointHistoryResponse> chargePointHistory(
        @PathVariable String memberKey,
        @RequestBody AddPointHistoryRequest request
    ) {
        AddPointHistoryDto dto = request.toAddPointHistoryDto(1);
        AddPointHistoryResponse response = pointHistoryService.addPointHistory(memberKey, dto);
        return ApiResponse.ok(response);
    }

    /**
     * 포인트 사용 API
     *
     * @param memberKey 회원 고유키
     * @param request 포인트 충전 요청 객체
     * @return 등록된 포인트 내역
     */
    @PostMapping("/use")
    public ApiResponse<AddPointHistoryResponse> usePointHistory(
        @PathVariable String memberKey,
        @RequestBody AddPointHistoryRequest request
    ) {
        AddPointHistoryDto dto = request.toAddPointHistoryDto(2);
        AddPointHistoryResponse response = pointHistoryService.addPointHistory(memberKey, dto);
        return ApiResponse.ok(response);
    }

    /**
     * 회원의 포인트 내역 조회 API
     *
     * @param memberKey 회원 고유키
     * @param pageNum 페이지 번호
     * @return 포인트 내역 페이징 조회 결과
     */
    @GetMapping
    public ApiResponse<Page<PointHistoryResponse>> getMyPointHistory(
        @PathVariable String memberKey,
        @RequestParam(defaultValue = "0") Integer pageNum
    ) {
        PageRequest request = PageRequest.of(pageNum, 15);
        Page<PointHistoryResponse> responses = pointHistoryQueryService.getMyPointHistories(memberKey, request);
        return ApiResponse.ok(responses);
    }
}
