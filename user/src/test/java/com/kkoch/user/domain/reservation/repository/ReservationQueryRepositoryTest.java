package com.kkoch.user.domain.reservation.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.domain.Grade;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import com.kkoch.user.domain.reservation.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.kkoch.user.domain.Grade.SUPER;
import static org.assertj.core.api.Assertions.*;

@Transactional
class ReservationQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원의 예약 내역을 조회할 수 있다.")
    @Test
    void findReservationByMemberKey() {
        //given
        Member member = createMember();
        Reservation reservation1 = createReservation(member, 1L);
        Reservation reservation2 = createReservation(member, 2L);
        Reservation reservation3 = createReservation(member, 3L);

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        List<Reservation> reservations = reservationQueryRepository.findReservationByMemberKey(member.getMemberKey(), pageRequest);

        //then
        assertThat(reservations).hasSize(2)
            .extracting("plantId")
            .containsExactlyInAnyOrder(2L, 3L);
    }

    @DisplayName("회원의 예약 내역 총 수를 조회할 수 있다.")
    @Test
    void getTotalCount() {
        //given
        Member member = createMember();
        Reservation reservation1 = createReservation(member, 1L);
        Reservation reservation2 = createReservation(member, 2L);
        Reservation reservation3 = createReservation(member, 3L);

        //when
        long totalCount = reservationQueryRepository.getTotalCount(member.getMemberKey());

        //then
        assertThat(totalCount).isEqualTo(3);
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

    private Reservation createReservation(Member member, Long plantId) {
        Reservation reservation = Reservation.builder()
            .count(10)
            .price(3000)
            .grade(SUPER)
            .member(member)
            .plantId(plantId)
            .build();
        return reservationRepository.save(reservation);
    }
}