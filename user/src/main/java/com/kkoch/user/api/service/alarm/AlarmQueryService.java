package com.kkoch.user.api.service.alarm;

import com.kkoch.user.api.controller.alarm.response.AlarmResponse;
import com.kkoch.user.domain.alarm.repository.AlarmQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 알림 Query 서비스
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AlarmQueryService {

    private final AlarmQueryRepository alarmQueryRepository;

    /**
     * 알림 조회
     *
     * @param memberKey 회원 고유키
     * @return 알림 정보 리스트
     */
    public List<AlarmResponse> searchAlarms(String memberKey) {
        return alarmQueryRepository.findAlarmsByMemberKey(memberKey);
    }
}
