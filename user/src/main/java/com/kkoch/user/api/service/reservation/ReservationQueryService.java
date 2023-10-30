package com.kkoch.user.api.service.reservation;

import com.kkoch.user.api.controller.reservation.response.ReservationForAuctionResponse;
import com.kkoch.user.api.controller.reservation.response.ReservationResponse;
import com.kkoch.user.client.PlantServiceClient;
import com.kkoch.user.client.response.PlantNameResponse;
import com.kkoch.user.domain.reservation.Reservation;
import com.kkoch.user.domain.reservation.repository.ReservationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReservationQueryService {

    private final ReservationQueryRepository reservationQueryRepository;
    private final PlantServiceClient plantServiceClient;

    public Page<ReservationResponse> getMyReservations(String memberKey, Pageable pageable) {
        List<Reservation> reservations = reservationQueryRepository.findReservationByMemberKey(memberKey, pageable);

        List<Long> ids = reservations.stream()
            .map(Reservation::getPlantId)
            .collect(Collectors.toList());

        Map<String, List<Long>> param = new HashMap<>();
        param.put("plantIds", ids);
        List<PlantNameResponse> plantNames = plantServiceClient.getPlantNames(param);

        Map<Long, PlantNameResponse> plantNameMap = plantNames.stream()
            .collect(Collectors.toMap(PlantNameResponse::getPlantId, plantNameResponse -> plantNameResponse, (a, b) -> b));

        List<ReservationResponse> responses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            PlantNameResponse plantName = plantNameMap.get(reservation.getPlantId());
            ReservationResponse response = ReservationResponse.of(reservation, plantName.getType(), plantName.getName());
            responses.add(response);
        }

        long totalCount = reservationQueryRepository.getTotalCount(memberKey);

        return new PageImpl<>(responses, pageable, totalCount);
    }

    public ReservationForAuctionResponse getReservation(Long plantId) {
        return reservationQueryRepository.findByPlantId(plantId);
    }
}
