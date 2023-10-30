package com.kkoch.user.domain.alarm.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.domain.alarm.Alarm;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Transactional
class AlarmCommandRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AlarmCommandRepository alarmCommandRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MemberRepository memberRepository;


    @DisplayName("회원의 알림이 모두 열람 상태가 된다.")
    @Test
    void updateOpen() {
        //given
        Member member = createMember();
        Alarm alarm1 = createAlarm(true, member);
        Alarm alarm2 = createAlarm(false, member);
        Alarm alarm3 = createAlarm(false, member);

        //when
        int count = alarmCommandRepository.updateOpen(member.getMemberKey());

        //then
        assertThat(count).isEqualTo(2);
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