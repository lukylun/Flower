package com.kkoch.user.api.service.pointhistory;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.pointhistory.response.PointHistoryResponse;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import com.kkoch.user.domain.pointhistory.PointHistory;
import com.kkoch.user.domain.pointhistory.repository.PointHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class PointHistoryQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private PointHistoryQueryService pointHistoryQueryService;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원은 본인의 포인트 내역을 조회할 수 있다.")
    @Test
    void getMyPointHistories() {
        //given
        Member member = createMember();
        createPointHistory(member, "신한은행", 10_000_000, 1);
        createPointHistory(member, "국민은행", 20_000_000, 1);
        createPointHistory(member, "하나은행", 30_000_000, 1);
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<PointHistoryResponse> responses = pointHistoryQueryService.getMyPointHistories(member.getMemberKey(), pageRequest);

        //then
        assertThat(responses.getContent()).hasSize(3);
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