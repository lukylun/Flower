package com.kkoch.user.api.service.alarm;

import com.kkoch.user.domain.alarm.repository.AlarmCommandRepository;
import com.kkoch.user.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 알림 Command 서비스
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AlamService {

    private final AlarmCommandRepository alarmCommandRepository;

    /**
     * 알림 열람
     *
     * @param memberKey 회원 고유키
     * @return 열람된 알림의 갯수
     */
    public int open(String memberKey) {
        return alarmCommandRepository.updateOpen(memberKey);
    }
}
