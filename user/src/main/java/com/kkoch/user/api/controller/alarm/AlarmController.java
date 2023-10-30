package com.kkoch.user.api.controller.alarm;

import com.kkoch.user.api.controller.ApiResponse;
import com.kkoch.user.api.controller.alarm.response.AlarmResponse;
import com.kkoch.user.api.service.alarm.AlamService;
import com.kkoch.user.api.service.alarm.AlarmQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

/**
 * 알림 API 컨트롤러
 *
 * @author 임우택
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/{memberKey}/alarms")
@Slf4j
public class AlarmController {

    private final AlamService alamService;
    private final AlarmQueryService alarmQueryService;

    /**
     * 알림 조회 및 열람 API
     *
     * @param memberKey 회원 식별키
     * @return 알림 정보 리스트
     */
    @GetMapping
    public ApiResponse<List<AlarmResponse>> getAlarms(@PathVariable String memberKey) {
        log.debug("call AlarmController#getAlarms={}", memberKey);

        List<AlarmResponse> responses = alarmQueryService.searchAlarms(memberKey);

        log.debug("response size = {}", responses.size());

        int openCount = alamService.open(memberKey);

        log.debug("openCount = {}", openCount);

        return ApiResponse.ok(responses);
    }
}
