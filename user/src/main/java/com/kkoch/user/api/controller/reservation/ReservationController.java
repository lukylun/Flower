package com.kkoch.user.api.controller.reservation;

import com.kkoch.user.api.controller.ApiResponse;
import com.kkoch.user.api.controller.reservation.request.AddReservationRequest;
import com.kkoch.user.api.controller.reservation.response.AddReservationResponse;
import com.kkoch.user.api.controller.reservation.response.ReservationResponse;
import com.kkoch.user.api.service.reservation.ReservationQueryService;
import com.kkoch.user.api.service.reservation.ReservationService;
import com.kkoch.user.api.service.reservation.dto.AddReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/{memberKey}/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationQueryService reservationQueryService;

    @PostMapping
    public ApiResponse<AddReservationResponse> addReservation(
        @Valid @RequestBody AddReservationRequest request,
        @PathVariable String memberKey
    ) {
        AddReservationDto dto = request.toAddReservationDto();
        AddReservationResponse response = reservationService.addReservation(memberKey, dto);

        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<Page<ReservationResponse>> reservations(
        @PathVariable String memberKey,
        @RequestParam(defaultValue = "0") Integer pageNum
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        Page<ReservationResponse> response = reservationQueryService.getMyReservations(memberKey, pageRequest);
        return ApiResponse.ok(response);
    }
}


