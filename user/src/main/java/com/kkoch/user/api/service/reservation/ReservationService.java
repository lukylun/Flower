package com.kkoch.user.api.service.reservation;

import com.kkoch.user.api.controller.reservation.response.AddReservationResponse;
import com.kkoch.user.api.service.reservation.dto.AddReservationDto;
import com.kkoch.user.client.PlantServiceClient;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import com.kkoch.user.domain.reservation.Reservation;
import com.kkoch.user.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    private final PlantServiceClient plantServiceClient;

    public AddReservationResponse addReservation(String memberKey, AddReservationDto dto) {
        Member member = memberRepository.findByMemberKey(memberKey)
            .orElseThrow(NoSuchElementException::new);

        Map<String, String> param = new HashMap<>();
        param.put("type", dto.getType());
        param.put("name", dto.getName());

        Long plantId = plantServiceClient.getPlantId(param);

        Reservation reservation = dto.toEntity(member, plantId);
        Reservation savedReservation = reservationRepository.save(reservation);

        return AddReservationResponse.of(savedReservation);
    }

    public void remove(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}