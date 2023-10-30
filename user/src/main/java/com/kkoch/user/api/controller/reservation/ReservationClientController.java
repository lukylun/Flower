package com.kkoch.user.api.controller.reservation;

import com.kkoch.user.api.controller.reservation.response.ReservationForAuctionResponse;
import com.kkoch.user.api.service.reservation.ReservationQueryService;
import com.kkoch.user.api.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client/reservation")
@Slf4j
public class ReservationClientController {

    private final ReservationService reservationService;
    private final ReservationQueryService reservationQueryService;

    @GetMapping("/{plantId}")
    public ReservationForAuctionResponse getReservationForAuction(@PathVariable Long plantId) {
        log.info("<컨트롤러 호출> ReservationClientController#getReservationForAuction = {}", plantId);
        return reservationQueryService.getReservation(plantId);
    }

    @DeleteMapping("/{reservationId}")
    public boolean removeReservation(@PathVariable Long reservationId) {
        log.info("예약 목록 삭제 = {}", reservationId);
        reservationService.remove(reservationId);
        return true;
    }
}
