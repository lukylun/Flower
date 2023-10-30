package com.kkoch.auction.client;

import com.kkoch.auction.client.response.ReservationForAuctionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/client/reservation/{plantId}")
    ReservationForAuctionResponse getReservationForAuction(@PathVariable Long plantId);

    @DeleteMapping("/client/reservation/{reservationId}")
    boolean removeReservation(@PathVariable Long reservationId);
}
