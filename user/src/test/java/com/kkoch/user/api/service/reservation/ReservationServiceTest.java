package com.kkoch.user.api.service.reservation;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.reservation.response.AddReservationResponse;
import com.kkoch.user.api.service.reservation.dto.AddReservationDto;
import com.kkoch.user.client.PlantServiceClient;
import com.kkoch.user.domain.Grade;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.kkoch.user.domain.Grade.SUPER;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.*;

@Transactional
class ReservationServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private PlantServiceClient plantServiceClient;

    @DisplayName("회원은 예약 등록을 할 수 있다.")
    @Test
    void addReservation() {
        //given
        Member member = createMember();
        AddReservationDto dto = AddReservationDto.builder()
            .type("type")
            .name("name")
            .count(10)
            .price(3000)
            .grade(SUPER)
            .build();

        given(plantServiceClient.getPlantId(anyMap()))
            .willReturn(1L);

        //when
        AddReservationResponse response = reservationService.addReservation(member.getMemberKey(), dto);

        //then
        Assertions.assertThat(response)
            .extracting("count", "price", "grade")
            .containsExactlyInAnyOrder(10, 3000, SUPER.getText());
    }

    private Member createMember() {
        Member member = Member.builder()
            .email("ssafy@ssafy.com")
            .encryptedPwd("password")
            .name("김싸피")
            .tel("010-1234-1234")
            .businessNumber("123-12-12345")
            .point(0)
            .active(true)
            .memberKey(UUID.randomUUID().toString())
            .build();
        return memberRepository.save(member);
    }
}