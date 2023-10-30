package com.kkoch.user.domain.alarm.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.alarm.response.AlarmResponse;
import com.kkoch.user.domain.alarm.Alarm;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
class AlarmQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AlarmQueryRepository alarmQueryRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원의 모든 알람을 조회할 수 있다.")
    @Test
    void findAlarmsByMemberKey() {
        //given
        Member member = createMember();
        Alarm alarm1 = createAlarm(true, member);
        Alarm alarm2 = createAlarm(false, member);
        Alarm alarm3 = createAlarm(false, member);

        //when
        List<AlarmResponse> responses = alarmQueryRepository.findAlarmsByMemberKey(member.getMemberKey());

        //then
        Assertions.assertThat(responses).hasSize(3)
            .extracting("open")
            .containsExactlyInAnyOrder(true, false, false);
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

    private Alarm createAlarm(boolean open, Member member) {
        Alarm alarm = Alarm.builder()
            .content("알림 내용")
            .open(open)
            .member(member)
            .build();
        return alarmRepository.save(alarm);
    }
}