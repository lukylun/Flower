package com.kkoch.user.domain.pointhistory.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.pointhistory.response.PointHistoryResponse;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import com.kkoch.user.domain.pointhistory.PointHistory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class PointHistoryQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private PointHistoryQueryRepository pointHistoryQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 식별키로 포인트 내역을 조회할 수 있다.")
    @Test
    void findByMemberKey() {
        //given
        Member member = createMember();
        createPointHistory(member, "신한은행", 10_000_000, 1);
        createPointHistory(member, "국민은행", 20_000_000, 1);
        createPointHistory(member, "하나은행", 30_000_000, 1);
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        List<PointHistoryResponse> responses = pointHistoryQueryRepository.findByMemberKey(member.getMemberKey(), pageRequest);

        //then
        assertThat(responses).hasSize(3)
            .extracting("bank", "amount")
            .containsExactlyInAnyOrder(
                tuple("신한은행", 10_000_000),
                tuple("국민은행", 20_000_000),
                tuple("하나은행", 30_000_000)
            );
    }

    @DisplayName("회원 식별키로 포인트 내역 전체 갯수를 조회할 수 있다.")
    @Test
    void getTotalCount() {
        //given
        Member member = createMember();
        createPointHistory(member, "신한은행", 10_000_000, 1);
        createPointHistory(member, "국민은행", 20_000_000, 1);
        createPointHistory(member, "하나은행", 30_000_000, 1);

        //when
        long totalCount = pointHistoryQueryRepository.getTotalCount(member.getMemberKey());

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

    private PointHistory createPointHistory(Member member, String bank, int amount, int status) {
        PointHistory pointHistory = PointHistory.builder()
            .bank(bank)
            .amount(amount)
            .status(status)
            .member(member)
            .build();
        return pointHistoryRepository.save(pointHistory);
    }
}